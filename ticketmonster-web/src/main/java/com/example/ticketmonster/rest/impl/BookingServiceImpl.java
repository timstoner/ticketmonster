package com.example.ticketmonster.rest.impl;

import java.net.URI;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.core.UriInfo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.example.ticketmonster.model.Booking;
import com.example.ticketmonster.model.Performance;
import com.example.ticketmonster.model.Section;
import com.example.ticketmonster.model.Ticket;
import com.example.ticketmonster.model.TicketCategory;
import com.example.ticketmonster.model.TicketPrice;
import com.example.ticketmonster.request.BookingRequest;
import com.example.ticketmonster.request.TicketRequest;
import com.example.ticketmonster.rest.BookingService;
import com.example.ticketmonster.rest.RestServiceException;
import com.example.ticketmonster.rest.SectionComparator;
import com.example.ticketmonster.rest.dto.BookingDTO;
import com.example.ticketmonster.service.AllocatedSeats;
import com.example.ticketmonster.service.SeatAllocationService;

/**
 * <p>
 * A JAX-RS endpoint for handling {@link Booking}s. Inherits the GET methods
 * from {@link BaseEntityService}, and implements additional REST methods.
 * </p>
 * 
 * @author Marius Bogoevici
 * @author Pete Muir
 */
@Path("/bookings")
public class BookingServiceImpl extends BaseEntityService<Booking> implements
		BookingService {

	private static Logger LOG = LoggerFactory
			.getLogger(BookingServiceImpl.class);

	@Autowired
	SeatAllocationService seatAllocationService;

	public BookingServiceImpl() {
		super(Booking.class);
	}

	@Override
	public Response findById(Long id) {
		Booking entity = getSingleInstance(id);

		if (entity != null) {
			BookingDTO dto = new BookingDTO(entity);
			return Response.ok(dto).build();
		} else {
			return Response.status(Status.NOT_FOUND).build();
		}
	}

	@Override
	public Response findAll(UriInfo uriInfo) {
		List<Booking> entities = this.getAll(uriInfo.getQueryParameters());
		List<BookingDTO> dtoResults = new ArrayList<>();

		// convert entities to data transfer objects
		for (Booking entity : entities) {
			BookingDTO dto = new BookingDTO(entity);
			dtoResults.add(dto);
		}

		return Response.ok(dtoResults).build();
	}

	@Override
	public Response deleteById(Long id) {
		LOG.debug("deleteById {}", id);
		return super.deleteById(id);
	}

	@Override
	public Response deleteAll(UriInfo uriInfo) {
		LOG.debug("deleteAll");
		return super.deleteAll(uriInfo);
	}

	@Override
	public Response create(BookingDTO dto) {
		// convert dto to entity
		Booking entity = dto.fromDTO(null, getEntityManager());
		// persist in database
		getEntityManager().persist(entity);
		// build uri to new entity
		String path = String.valueOf(entity.getId());
		URI uri = UriBuilder.fromResource(BookingService.class).path(path)
				.build();

		return Response.created(uri).build();
	}

	@Override
	public Response update(Long id, BookingDTO dto) {
		LOG.debug("update {}", id);
		Booking entity = getSingleInstance(id);

		entity = dto.fromDTO(entity, getEntityManager());
		entity = getEntityManager().merge(entity);

		return Response.noContent().build();
	}

	/**
	 * <p>
	 * Create a booking. Data is contained in the bookingRequest object
	 * </p>
	 * 
	 * @param bookingRequest
	 * @return
	 */
	@POST
	/**
	 * <p> Data is received in JSON format. For easy handling, it will be unmarshalled in the support
	 * {@link BookingRequest} class.
	 */
	@Consumes(MediaType.APPLICATION_JSON)
	public Response createBooking(BookingRequest bookingRequest) {
		try {
			// identify the ticket price categories in this request
			Set<Long> priceCategoryIds = bookingRequest
					.getUniquePriceCategoryIds();

			// load the entities that make up this booking's relationships
			Performance performance = getEntityManager().find(
					Performance.class, bookingRequest.getPerformance());

			// As we can have a mix of ticket types in a booking, we need to
			// load all of them that are relevant,
			// id
			Map<Long, TicketPrice> ticketPricesById = loadTicketPrices(priceCategoryIds);

			// Now, start to create the booking from the posted data
			// Set the simple stuff first!
			Booking booking = new Booking();
			booking.setContactEmail(bookingRequest.getEmail());
			booking.setPerformance(performance);
			booking.setCancellationCode("abc");

			// Now, we iterate over each ticket that was requested, and organize
			// them by section and category
			// we want to allocate ticket requests that belong to the same
			// section contiguously
			Map<Section, Map<TicketCategory, TicketRequest>> ticketRequestsPerSection = new TreeMap<Section, java.util.Map<TicketCategory, TicketRequest>>(
					SectionComparator.instance());
			for (TicketRequest ticketRequest : bookingRequest
					.getTicketRequests()) {
				final TicketPrice ticketPrice = ticketPricesById
						.get(ticketRequest.getTicketPrice());
				if (!ticketRequestsPerSection.containsKey(ticketPrice
						.getSection())) {
					ticketRequestsPerSection.put(ticketPrice.getSection(),
							new HashMap<TicketCategory, TicketRequest>());
				}
				ticketRequestsPerSection.get(ticketPrice.getSection()).put(
						ticketPricesById.get(ticketRequest.getTicketPrice())
								.getTicketCategory(), ticketRequest);
			}

			// Now, we can allocate the tickets
			// Iterate over the sections, finding the candidate seats for
			// allocation
			// The process will lock the record for a given
			// Use deterministic ordering to prevent deadlocks
			Map<Section, AllocatedSeats> seatsPerSection = new TreeMap<Section, AllocatedSeats>(
					SectionComparator.instance());
			List<Section> failedSections = new ArrayList<Section>();
			for (Section section : ticketRequestsPerSection.keySet()) {
				int totalTicketsRequestedPerSection = 0;
				// Compute the total number of tickets required (a ticket
				// category doesn't impact the actual seat!)
				final Map<TicketCategory, TicketRequest> ticketRequestsByCategories = ticketRequestsPerSection
						.get(section);
				// calculate the total quantity of tickets to be allocated in
				// this section
				for (TicketRequest ticketRequest : ticketRequestsByCategories
						.values()) {
					totalTicketsRequestedPerSection += ticketRequest
							.getQuantity();
				}
				// try to allocate seats

				AllocatedSeats allocatedSeats = seatAllocationService
						.allocateSeats(section, performance,
								totalTicketsRequestedPerSection, true);
				if (allocatedSeats.getSeats().size() == totalTicketsRequestedPerSection) {
					seatsPerSection.put(section, allocatedSeats);
				} else {
					failedSections.add(section);
				}
			}
			if (failedSections.isEmpty()) {
				for (Section section : seatsPerSection.keySet()) {
					// allocation was successful, begin generating tickets
					// associate each allocated seat with a ticket, assigning a
					// price category to it
					final Map<TicketCategory, TicketRequest> ticketRequestsByCategories = ticketRequestsPerSection
							.get(section);
					AllocatedSeats allocatedSeats = seatsPerSection
							.get(section);
					allocatedSeats.markOccupied();
					int seatCounter = 0;
					// Now, add a ticket for each requested ticket to the
					// booking
					for (TicketCategory ticketCategory : ticketRequestsByCategories
							.keySet()) {
						final TicketRequest ticketRequest = ticketRequestsByCategories
								.get(ticketCategory);
						final TicketPrice ticketPrice = ticketPricesById
								.get(ticketRequest.getTicketPrice());
						for (int i = 0; i < ticketRequest.getQuantity(); i++) {
							Ticket ticket = new Ticket(allocatedSeats
									.getSeats().get(seatCounter + i),
									ticketCategory, ticketPrice.getPrice());
							// getEntityManager().persist(ticket);
							booking.getTickets().add(ticket);
						}
						seatCounter += ticketRequest.getQuantity();
					}
				}
				// Persist the booking, including cascaded relationships
				booking.setPerformance(performance);
				booking.setCancellationCode("abc");
				getEntityManager().persist(booking);
				// newBookingEvent.fire(booking);
				return Response.ok().entity(booking)
						.type(MediaType.APPLICATION_JSON).build();
			} else {
				Map<String, Object> responseEntity = new HashMap<String, Object>();
				responseEntity
						.put("errors",
								Collections
										.singletonList("Cannot allocate the requested number of seats!"));
				return Response.status(Response.Status.BAD_REQUEST)
						.entity(responseEntity).build();
			}
		} catch (ConstraintViolationException e) {
			// If validation of the data failed using Bean Validation, then send
			// an error
			Map<String, Object> errors = new HashMap<String, Object>();
			List<String> errorMessages = new ArrayList<String>();
			for (ConstraintViolation<?> constraintViolation : e
					.getConstraintViolations()) {
				errorMessages.add(constraintViolation.getMessage());
			}
			errors.put("errors", errorMessages);
			// A WebApplicationException can wrap a response
			// Throwing the exception causes an automatic rollback
			throw new RestServiceException(Response
					.status(Response.Status.BAD_REQUEST).entity(errors).build());
		} catch (Exception e) {
			// Finally, handle unexpected exceptions
			Map<String, Object> errors = new HashMap<String, Object>();
			errors.put("errors", Collections.singletonList(e.getMessage()));
			// A WebApplicationException can wrap a response
			// Throwing the exception causes an automatic rollback
			throw new RestServiceException(Response
					.status(Response.Status.BAD_REQUEST).entity(errors).build());
		}
	}

	private Map<Long, TicketPrice> loadTicketPrices(Set<Long> priceCategoryIds) {
		List<TicketPrice> ticketPrices = (List<TicketPrice>) getEntityManager()
				.createQuery("select p from TicketPrice p where p.id in :ids",
						TicketPrice.class)
				.setParameter("ids", priceCategoryIds).getResultList();
		// Now, map them by id
		Map<Long, TicketPrice> ticketPricesById = new HashMap<Long, TicketPrice>();
		for (TicketPrice ticketPrice : ticketPrices) {
			ticketPricesById.put(ticketPrice.getId(), ticketPrice);
		}
		return ticketPricesById;
	}

	protected String getFindByIdQuery() {
		return "SELECT DISTINCT b FROM Booking b LEFT JOIN FETCH b.tickets LEFT JOIN FETCH b.performance WHERE b.id = :entityId ORDER BY b.id";
	}

	protected String getFindAllQuery() {
		return "SELECT DISTINCT b FROM Booking b LEFT JOIN FETCH b.tickets LEFT JOIN FETCH b.performance ORDER BY b.id";
	}
}

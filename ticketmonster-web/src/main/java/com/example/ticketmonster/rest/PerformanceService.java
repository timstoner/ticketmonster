package com.example.ticketmonster.rest;

import java.net.URI;
import java.util.List;

import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.transaction.Transactional;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.UriBuilder;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.example.ticketmonster.model.Booking;
import com.example.ticketmonster.model.Performance;
import com.example.ticketmonster.model.SectionAllocation;
import com.example.ticketmonster.model.Show;
import com.example.ticketmonster.rest.dto.PerformanceDTO;

@Path("/performances")
public class PerformanceService extends
		BaseEntityService<Performance, PerformanceDTO> {

	private static final Logger LOG = LoggerFactory
			.getLogger(PerformanceService.class);

	public PerformanceService() {
		super(Performance.class, PerformanceDTO.class);
	}

	@POST
	@Consumes("application/json")
	@Transactional
	public Response create(PerformanceDTO dto) {
		// convert performance dto to a performance entity
		Performance entity = dto.fromDTO(null, getEntityManager());
		// persist the performance to the database
		getEntityManager().persist(entity);

		// build the performance uri for future requests
		String path = String.valueOf(entity.getId());
		URI uri = UriBuilder.fromResource(PerformanceService.class).path(path)
				.build();
		LOG.info("Performance created path: {}", path);

		// return 201 'created' response
		return Response.created(uri).build();
	}

	@DELETE
	@Path("/{id:[0-9][0-9]*}")
	@Transactional
	public Response deleteById(@PathParam("id") Long id) {
		LOG.debug("deleteById {}", id);
		// find the performance by id
		Performance entity = getEntityManager().find(Performance.class, id);
		ResponseBuilder rb;

		// if the entity exists
		if (entity != null) {
			// get the show from performance
			Show show = entity.getShow();
			// remove all performance from database
			show.getPerformances().remove(entity);
			// null out the show
			entity.setShow(null);

			getEntityManager().merge(show);

			// delete all section allocations for this performance
			List<SectionAllocation> sectionAllocations = findSectionAllocationsByPerformance(entity);
			for (SectionAllocation sectionAllocation : sectionAllocations) {
				getEntityManager().remove(sectionAllocation);
			}

			// delete all bookings for this performance
			List<Booking> bookings = findBookingsByPerformance(entity);
			for (Booking booking : bookings) {
				getEntityManager().remove(booking);
			}

			// delete the performance from the database
			getEntityManager().remove(entity);
			// set response to no content
			rb = Response.noContent();
		} else {
			// if no id found, log message and return not found
			LOG.info("Delete failed, No Id Found: {}", id);
			rb = Response.status(Status.NOT_FOUND);
		}

		return rb.build();
	}

	@GET
	@Path("/{id:[0-9][0-9]*}")
	@Produces("application/json")
	public Response findById(@PathParam("id") Long id) {
		ResponseBuilder rb;

		TypedQuery<Performance> findByIdQuery = getEntityManager()
				.createQuery(
						"SELECT DISTINCT p FROM Performance p LEFT JOIN FETCH p.show WHERE p.id = :entityId ORDER BY p.id",
						Performance.class);
		findByIdQuery.setParameter("entityId", id);
		Performance entity;

		// try to find a single result from the db
		try {
			entity = findByIdQuery.getSingleResult();
		} catch (NoResultException nre) {
			entity = null;
		}

		// if an entity was found in the db
		if (entity != null) {
			// convert to a dto
			PerformanceDTO dto = new PerformanceDTO(entity);
			// build an OK response with the DTO
			rb = Response.ok(dto);
		} else {
			rb = Response.status(Status.NOT_FOUND);
		}

		return rb.build();
	}

	// @GET
	// @Produces("application/json")
	// public List<PerformanceDTO> listAll() {
	// final List<Performance> searchResults = getEntityManager()
	// .createQuery(
	// "SELECT DISTINCT p FROM Performance p LEFT JOIN FETCH p.show ORDER BY p.id",
	// Performance.class).getResultList();
	// final List<PerformanceDTO> results = new ArrayList<PerformanceDTO>();
	// for (Performance searchResult : searchResults) {
	// PerformanceDTO dto = new PerformanceDTO(searchResult);
	// results.add(dto);
	// }
	// return results;
	// }

	@PUT
	@Path("/{id:[0-9][0-9]*}")
	@Consumes("application/json")
	public Response update(@PathParam("id") Long id, PerformanceDTO dto) {
		LOG.debug("update {}", id);

		TypedQuery<Performance> findByIdQuery = getEntityManager()
				.createQuery(
						"SELECT DISTINCT p FROM Performance p LEFT JOIN FETCH p.show WHERE p.id = :entityId ORDER BY p.id",
						Performance.class);
		findByIdQuery.setParameter("entityId", id);

		Performance entity;
		try {
			entity = findByIdQuery.getSingleResult();
		} catch (NoResultException nre) {
			// if no result found, set entity to null
			// create a new entity
			entity = null;
		}

		// convert dto to entity
		entity = dto.fromDTO(entity, getEntityManager());
		// merge data from dto to database
		entity = getEntityManager().merge(entity);

		return Response.noContent().build();
	}

	public List<SectionAllocation> findSectionAllocationsByPerformance(
			Performance performance) {
		CriteriaQuery<SectionAllocation> criteria = getEntityManager()
				.getCriteriaBuilder().createQuery(SectionAllocation.class);
		Root<SectionAllocation> from = criteria.from(SectionAllocation.class);
		CriteriaBuilder builder = getEntityManager().getCriteriaBuilder();
		Predicate performanceIsSame = builder.equal(from.get("performance"),
				performance);
		return getEntityManager().createQuery(
				criteria.select(from).where(performanceIsSame)).getResultList();
	}

	public List<Booking> findBookingsByPerformance(Performance performance) {
		CriteriaQuery<Booking> criteria = getEntityManager()
				.getCriteriaBuilder().createQuery(Booking.class);
		Root<Booking> from = criteria.from(Booking.class);
		CriteriaBuilder builder = getEntityManager().getCriteriaBuilder();
		Predicate performanceIsSame = builder.equal(from.get("performance"),
				performance);
		return getEntityManager().createQuery(
				criteria.select(from).where(performanceIsSame)).getResultList();
	}

}

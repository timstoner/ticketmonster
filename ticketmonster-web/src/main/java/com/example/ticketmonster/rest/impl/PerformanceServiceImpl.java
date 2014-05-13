package com.example.ticketmonster.rest.impl;

import java.net.URI;
import java.util.ArrayList;
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
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.core.Response.ResponseBuilder;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.UriBuilder;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.example.ticketmonster.model.Booking;
import com.example.ticketmonster.model.Performance;
import com.example.ticketmonster.model.SectionAllocation;
import com.example.ticketmonster.model.Show;
import com.example.ticketmonster.rest.PerformanceService;
import com.example.ticketmonster.rest.dto.BookingDTO;
import com.example.ticketmonster.rest.dto.PerformanceDTO;

public class PerformanceServiceImpl extends BaseEntityService<Performance>
		implements PerformanceService {

	private static final Logger LOG = LoggerFactory
			.getLogger(PerformanceServiceImpl.class);

	public PerformanceServiceImpl() {
		super(Performance.class);
	}

	public Response create(PerformanceDTO dto) {
		// convert performance dto to a performance entity
		Performance entity = dto.fromDTO(null, getEntityManager());
		// persist the performance to the database
		getEntityManager().persist(entity);

		// build the performance uri for future requests
		String path = String.valueOf(entity.getId());
		URI uri = UriBuilder.fromResource(PerformanceServiceImpl.class)
				.path(path).build();
		LOG.info("Performance created path: {}", path);

		// return 201 'created' response
		return Response.created(uri).build();
	}

	public Response deleteById(Long id) {
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

	public Response findById(Long id) {
		ResponseBuilder rb;

		Performance entity = getSingleInstance(id);

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

	@Override
	public Response findAll(UriInfo uriInfo) {
		List<Performance> searchResults = this.getAll(uriInfo
				.getQueryParameters());
		List<PerformanceDTO> dtoResults = new ArrayList<>();

		// convert entities to data transfer objects
		for (Performance entity : searchResults) {
			PerformanceDTO dto = new PerformanceDTO(entity);
			dtoResults.add(dto);
		}

		return Response.ok(dtoResults).build();
	}

	public Response update(Long id, PerformanceDTO dto) {
		LOG.debug("update {}", id);

		Performance entity = getSingleInstance(id);
		if (entity != null) {
			// convert dto to entity
			entity = dto.fromDTO(entity, getEntityManager());
			// merge data from dto to database
			entity = getEntityManager().merge(entity);
		}

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

	@Override
	protected String getFindByIdQuery() {
		return "SELECT DISTINCT p FROM Performance p LEFT JOIN FETCH p.show WHERE p.id = :entityId ORDER BY p.id";
	}

	@Override
	protected String getFindAllQuery() {
		return "SELECT DISTINCT p FROM Performance p LEFT JOIN FETCH p.show ORDER BY p.id";
	}
}

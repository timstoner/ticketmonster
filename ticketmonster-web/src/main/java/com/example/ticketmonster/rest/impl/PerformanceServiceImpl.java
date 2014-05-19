package com.example.ticketmonster.rest.impl;

import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;
import javax.ws.rs.core.Response.Status;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.example.ticketmonster.dto.PerformanceDTO;
import com.example.ticketmonster.factory.PerformanceFactory;
import com.example.ticketmonster.model.Booking;
import com.example.ticketmonster.model.Performance;
import com.example.ticketmonster.model.SectionAllocation;
import com.example.ticketmonster.model.Show;
import com.example.ticketmonster.rest.PerformanceService;

public class PerformanceServiceImpl extends
		BaseEntityService<Performance, PerformanceDTO> implements
		PerformanceService {

	private static final Logger LOG = LoggerFactory
			.getLogger(PerformanceServiceImpl.class);

	public PerformanceServiceImpl() {
		super(Performance.class);
		LOG.debug("Creating Performance Service");
	}

	@Override
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
	protected Performance buildEntity(PerformanceDTO dto) {
		Performance entity = PerformanceFactory.buildEntity(dto, em);
		return entity;
	}

	@Override
	protected String getFindAllQuery() {
		return PerformanceFactory.getFindAllQuery();
	}

	@Override
	protected String getFindByIdQuery() {
		return PerformanceFactory.getFindByIdQuery();
	}
}

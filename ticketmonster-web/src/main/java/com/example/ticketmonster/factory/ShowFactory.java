package com.example.ticketmonster.factory;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import com.example.ticketmonster.dto.NestedPerformanceDTO;
import com.example.ticketmonster.dto.NestedShowDTO;
import com.example.ticketmonster.dto.NestedTicketPriceDTO;
import com.example.ticketmonster.dto.ShowDTO;
import com.example.ticketmonster.model.Booking;
import com.example.ticketmonster.model.Event;
import com.example.ticketmonster.model.Performance;
import com.example.ticketmonster.model.SectionAllocation;
import com.example.ticketmonster.model.Show;
import com.example.ticketmonster.model.TicketPrice;
import com.example.ticketmonster.model.Venue;

public class ShowFactory extends EntityFactory {

	public static Show buildEntity(ShowDTO dto, EntityManager em) {
		Show entity;

		entity = new Show();
		Event event = EventFactory.buildEntity(dto.getEvent(), em);
		entity.setEvent(event);

		Performance performance;
		for (NestedPerformanceDTO perfDTO : dto.getPerformances()) {
			performance = PerformanceFactory.buildEntity(perfDTO, em);
			entity.getPerformances().add(performance);
		}

		TicketPrice ticketPrice;
		for (NestedTicketPriceDTO ticketDTO : dto.getTicketPrices()) {
			ticketPrice = TicketPriceFactory.buildEntity(ticketDTO, em);
			entity.getTicketPrices().add(ticketPrice);
		}

		Venue venue = VenueFactory.buildEntity(dto.getVenue(), em);
		entity.setVenue(venue);

		return entity;
	}

	public static Show buildEntity(NestedShowDTO dto, EntityManager em) {
		Show entity;

		entity = findEntity(dto.getId(), em, Show.class, getFindByIdQuery());

		return entity;
	}

	public static String getFindByIdQuery() {
		return "SELECT DISTINCT s FROM Show s LEFT JOIN FETCH s.event LEFT JOIN FETCH s.venue LEFT JOIN FETCH s.performances LEFT JOIN FETCH s.ticketPrices WHERE s.id = :entityId ORDER BY s.id";
	}

	public static String getFindAllQuery() {
		return "SELECT DISTINCT s FROM Show s LEFT JOIN FETCH s.event LEFT JOIN FETCH s.venue LEFT JOIN FETCH s.performances LEFT JOIN FETCH s.ticketPrices ORDER BY s.id";
	}

	public static List<SectionAllocation> findSectionAllocationsByPerformance(
			Performance performance, EntityManager em) {
		CriteriaQuery<SectionAllocation> criteria = em.getCriteriaBuilder()
				.createQuery(SectionAllocation.class);
		Root<SectionAllocation> from = criteria.from(SectionAllocation.class);
		CriteriaBuilder builder = em.getCriteriaBuilder();
		Predicate performanceIsSame = builder.equal(from.get("performance"),
				performance);
		return em.createQuery(criteria.select(from).where(performanceIsSame))
				.getResultList();
	}

	public static List<Booking> findBookingsByPerformance(
			Performance performance, EntityManager em) {
		CriteriaQuery<Booking> criteria = em.getCriteriaBuilder().createQuery(
				Booking.class);
		Root<Booking> from = criteria.from(Booking.class);
		CriteriaBuilder builder = em.getCriteriaBuilder();
		Predicate performanceIsSame = builder.equal(from.get("performance"),
				performance);
		return em.createQuery(criteria.select(from).where(performanceIsSame))
				.getResultList();
	}
}

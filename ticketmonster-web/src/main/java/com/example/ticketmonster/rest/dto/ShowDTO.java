package com.example.ticketmonster.rest.dto;

import java.io.Serializable;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import com.example.ticketmonster.model.Booking;
import com.example.ticketmonster.model.Performance;
import com.example.ticketmonster.model.SectionAllocation;
import com.example.ticketmonster.model.Show;
import com.example.ticketmonster.model.TicketPrice;
import com.example.ticketmonster.rest.dto.NestedEventDTO;
import com.example.ticketmonster.rest.dto.NestedPerformanceDTO;
import com.example.ticketmonster.rest.dto.NestedTicketPriceDTO;
import com.example.ticketmonster.rest.dto.NestedVenueDTO;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class ShowDTO extends BaseDTO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Long id;
	private Set<NestedTicketPriceDTO> ticketPrices = new HashSet<NestedTicketPriceDTO>();
	private NestedEventDTO event;
	private Set<NestedPerformanceDTO> performances = new HashSet<NestedPerformanceDTO>();
	private NestedVenueDTO venue;
	private String displayTitle;

	public ShowDTO() {
	}

	public ShowDTO(final Show entity) {
		if (entity != null) {
			this.id = entity.getId();
			Iterator<TicketPrice> iterTicketPrices = entity.getTicketPrices()
					.iterator();
			for (; iterTicketPrices.hasNext();) {
				TicketPrice element = iterTicketPrices.next();
				this.ticketPrices.add(new NestedTicketPriceDTO(element));
			}
			this.event = new NestedEventDTO(entity.getEvent());
			Iterator<Performance> iterPerformances = entity.getPerformances()
					.iterator();
			for (; iterPerformances.hasNext();) {
				Performance element = iterPerformances.next();
				this.performances.add(new NestedPerformanceDTO(element));
			}
			this.venue = new NestedVenueDTO(entity.getVenue());
			this.displayTitle = entity.toString();
		}
	}

	public Show fromDTO(Show entity, EntityManager em) {
		if (entity == null) {
			entity = new Show();
		}
		Iterator<TicketPrice> iterTicketPrices = entity.getTicketPrices()
				.iterator();
		for (; iterTicketPrices.hasNext();) {
			boolean found = false;
			TicketPrice ticketPrice = iterTicketPrices.next();
			Iterator<NestedTicketPriceDTO> iterDtoTicketPrices = this
					.getTicketPrices().iterator();
			for (; iterDtoTicketPrices.hasNext();) {
				NestedTicketPriceDTO dtoTicketPrice = iterDtoTicketPrices
						.next();
				if (dtoTicketPrice.getId().equals(ticketPrice.getId())) {
					found = true;
					break;
				}
			}
			if (found == false) {
				iterTicketPrices.remove();
				em.remove(ticketPrice);
			}
		}
		Iterator<NestedTicketPriceDTO> iterDtoTicketPrices = this
				.getTicketPrices().iterator();
		for (; iterDtoTicketPrices.hasNext();) {
			boolean found = false;
			NestedTicketPriceDTO dtoTicketPrice = iterDtoTicketPrices.next();
			iterTicketPrices = entity.getTicketPrices().iterator();
			for (; iterTicketPrices.hasNext();) {
				TicketPrice ticketPrice = iterTicketPrices.next();
				if (dtoTicketPrice.getId().equals(ticketPrice.getId())) {
					found = true;
					break;
				}
			}
			if (found == false) {
				Iterator<TicketPrice> resultIter = em
						.createQuery("SELECT DISTINCT t FROM TicketPrice t",
								TicketPrice.class).getResultList().iterator();
				for (; resultIter.hasNext();) {
					TicketPrice result = resultIter.next();
					if (result.getId().equals(dtoTicketPrice.getId())) {
						entity.getTicketPrices().add(result);
						break;
					}
				}
			}
		}
		if (this.event != null) {
			entity.setEvent(this.event.fromDTO(entity.getEvent(), em));
		}
		Iterator<Performance> iterPerformances = entity.getPerformances()
				.iterator();
		for (; iterPerformances.hasNext();) {
			boolean found = false;
			Performance performance = iterPerformances.next();
			Iterator<NestedPerformanceDTO> iterDtoPerformances = this
					.getPerformances().iterator();
			for (; iterDtoPerformances.hasNext();) {
				NestedPerformanceDTO dtoPerformance = iterDtoPerformances
						.next();
				if (dtoPerformance.getId().equals(performance.getId())) {
					found = true;
					break;
				}
			}
			if (found == false) {
				iterPerformances.remove();
				List<SectionAllocation> sectionAllocations = findSectionAllocationsByPerformance(
						performance, em);
				for (SectionAllocation sectionAllocation : sectionAllocations) {
					em.remove(sectionAllocation);
				}
				List<Booking> bookings = findBookingsByPerformance(performance,
						em);
				for (Booking booking : bookings) {
					em.remove(booking);
				}
				em.remove(performance);
			}
		}
		Iterator<NestedPerformanceDTO> iterDtoPerformances = this
				.getPerformances().iterator();
		for (; iterDtoPerformances.hasNext();) {
			boolean found = false;
			NestedPerformanceDTO dtoPerformance = iterDtoPerformances.next();
			iterPerformances = entity.getPerformances().iterator();
			for (; iterPerformances.hasNext();) {
				Performance performance = iterPerformances.next();
				if (dtoPerformance.getId().equals(performance.getId())) {
					found = true;
					break;
				}
			}
			if (found == false) {
				Iterator<Performance> resultIter = em
						.createQuery("SELECT DISTINCT p FROM Performance p",
								Performance.class).getResultList().iterator();
				for (; resultIter.hasNext();) {
					Performance result = resultIter.next();
					if (result.getId().equals(dtoPerformance.getId())) {
						entity.getPerformances().add(result);
						break;
					}
				}
			}
		}
		if (this.venue != null) {
			entity.setVenue(this.venue.fromDTO(entity.getVenue(), em));
		}
		entity = em.merge(entity);
		return entity;
	}

	public List<SectionAllocation> findSectionAllocationsByPerformance(
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

	public List<Booking> findBookingsByPerformance(Performance performance,
			EntityManager em) {
		CriteriaQuery<Booking> criteria = em.getCriteriaBuilder().createQuery(
				Booking.class);
		Root<Booking> from = criteria.from(Booking.class);
		CriteriaBuilder builder = em.getCriteriaBuilder();
		Predicate performanceIsSame = builder.equal(from.get("performance"),
				performance);
		return em.createQuery(criteria.select(from).where(performanceIsSame))
				.getResultList();
	}

	public Long getId() {
		return this.id;
	}

	public void setId(final Long id) {
		this.id = id;
	}

	public Set<NestedTicketPriceDTO> getTicketPrices() {
		return this.ticketPrices;
	}

	public void setTicketPrices(final Set<NestedTicketPriceDTO> ticketPrices) {
		this.ticketPrices = ticketPrices;
	}

	public NestedEventDTO getEvent() {
		return this.event;
	}

	public void setEvent(final NestedEventDTO event) {
		this.event = event;
	}

	public Set<NestedPerformanceDTO> getPerformances() {
		return this.performances;
	}

	public void setPerformances(final Set<NestedPerformanceDTO> performances) {
		this.performances = performances;
	}

	public NestedVenueDTO getVenue() {
		return this.venue;
	}

	public void setVenue(final NestedVenueDTO venue) {
		this.venue = venue;
	}

	public String getDisplayTitle() {
		return this.displayTitle;
	}
}

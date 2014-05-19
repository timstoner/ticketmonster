package com.example.ticketmonster.dto;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class ShowDTO extends BaseDTO implements Serializable {

	private static final long serialVersionUID = 1L;
	private Long id;
	private Set<NestedTicketPriceDTO> ticketPrices = new HashSet<NestedTicketPriceDTO>();
	private NestedEventDTO event;
	private Set<NestedPerformanceDTO> performances = new HashSet<NestedPerformanceDTO>();
	private NestedVenueDTO venue;
	private String displayTitle;

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

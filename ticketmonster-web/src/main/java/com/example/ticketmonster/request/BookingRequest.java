package com.example.ticketmonster.request;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.example.ticketmonster.model.Performance;
import com.example.ticketmonster.rest.impl.BookingServiceImpl;

/**
 * <p>
 * A {@link BookingRequest} is populated with unmarshalled JSON data, and handed
 * to {@link BookingServiceImpl#createBooking(BookingRequest)}.
 * </p>
 * 
 * @author Marius Bogoevici
 * @author Pete Muir
 * 
 */
public class BookingRequest {

	private List<TicketRequest> ticketRequests = new ArrayList<TicketRequest>();
	private long performance;
	private String email;

	public BookingRequest() {
		// Empty constructor for JAXB
	}

	public BookingRequest(Performance performance, String email) {
		this.performance = performance.getId();
		this.email = email;
	}

	public List<TicketRequest> getTicketRequests() {
		return ticketRequests;
	}

	public void setTicketRequests(List<TicketRequest> ticketRequests) {
		this.ticketRequests = ticketRequests;
	}

	public BookingRequest addTicketRequest(TicketRequest ticketRequest) {
		ticketRequests.add(ticketRequest);
		return this;
	}

	public long getPerformance() {
		return performance;
	}

	public void setPerformance(long performance) {
		this.performance = performance;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String toString() {
		StringBuilder sb = new StringBuilder();

		sb.append("Booking Request id: ").append(performance)
				.append(" Email: ").append(email);

		return sb.toString();
	}

	/**
	 * Utility method - computes the unique price category ids in the request
	 * 
	 * @return
	 */
	public Set<Long> getUniquePriceCategoryIds() {
		Set<Long> priceCategoryIds = new HashSet<Long>();
		for (TicketRequest ticketRequest : getTicketRequests()) {
			if (priceCategoryIds.contains(ticketRequest.getTicketPrice())) {
				throw new RuntimeException("Duplicate price category id");
			}
			priceCategoryIds.add(ticketRequest.getTicketPrice());
		}
		return priceCategoryIds;
	}
	
	
}

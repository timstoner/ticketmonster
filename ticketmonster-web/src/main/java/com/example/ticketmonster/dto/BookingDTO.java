package com.example.ticketmonster.dto;

import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.xml.bind.annotation.XmlRootElement;

import com.example.ticketmonster.model.Booking;
import com.example.ticketmonster.model.Ticket;

@XmlRootElement
public class BookingDTO extends BaseDTO implements Serializable {

	private static final long serialVersionUID = 1L;
	private Date createdOn;
	private Long id;
	private String cancellationCode;
	private String contactEmail;
	private NestedPerformanceDTO performance;
	private float totalTicketPrice;
	private Set<NestedTicketDTO> tickets = new HashSet<NestedTicketDTO>();

	public Booking fromDTO(Booking entity, EntityManager em) {
		if (entity == null) {
			entity = new Booking();
		}

		entity.setCreatedOn(this.createdOn);
		entity.setCancellationCode(this.cancellationCode);
		entity.setContactEmail(this.contactEmail);

		if (this.performance != null) {
			entity.setPerformance(this.performance.fromDTO(
					entity.getPerformance(), em));
		}
		Iterator<Ticket> iterTickets = entity.getTickets().iterator();
		for (; iterTickets.hasNext();) {
			boolean found = false;
			Ticket ticket = iterTickets.next();
			Iterator<NestedTicketDTO> iterDtoTickets = this.getTickets()
					.iterator();
			for (; iterDtoTickets.hasNext();) {
				NestedTicketDTO dtoTicket = iterDtoTickets.next();
				if (dtoTicket.getId().equals(ticket.getId())) {
					found = true;
					break;
				}
			}
			if (found == false) {
				iterTickets.remove();
				em.remove(ticket);
			}
		}
		Iterator<NestedTicketDTO> iterDtoTickets = this.getTickets().iterator();
		for (; iterDtoTickets.hasNext();) {
			boolean found = false;
			NestedTicketDTO dtoTicket = iterDtoTickets.next();
			iterTickets = entity.getTickets().iterator();
			for (; iterTickets.hasNext();) {
				Ticket ticket = iterTickets.next();
				if (dtoTicket.getId().equals(ticket.getId())) {
					found = true;
					break;
				}
			}
			if (found == false) {
				Iterator<Ticket> resultIter = em
						.createQuery("SELECT DISTINCT t FROM Ticket t",
								Ticket.class).getResultList().iterator();
				for (; resultIter.hasNext();) {
					Ticket result = resultIter.next();
					if (result.getId().equals(dtoTicket.getId())) {
						entity.getTickets().add(result);
						break;
					}
				}
			}
		}
		entity = em.merge(entity);
		return entity;
	}

	public Date getCreatedOn() {
		return this.createdOn;
	}

	public void setCreatedOn(final Date createdOn) {
		this.createdOn = createdOn;
	}

	public Long getId() {
		return this.id;
	}

	public void setId(final Long id) {
		this.id = id;
	}

	public String getCancellationCode() {
		return this.cancellationCode;
	}

	public void setCancellationCode(final String cancellationCode) {
		this.cancellationCode = cancellationCode;
	}

	public String getContactEmail() {
		return this.contactEmail;
	}

	public void setContactEmail(final String contactEmail) {
		this.contactEmail = contactEmail;
	}

	public NestedPerformanceDTO getPerformance() {
		return this.performance;
	}

	public void setPerformance(final NestedPerformanceDTO performance) {
		this.performance = performance;
	}

	public float getTotalTicketPrice() {
		return this.totalTicketPrice;
	}

	public void setTotalTicketPrice(final float totalTicketPrice) {
		this.totalTicketPrice = totalTicketPrice;
	}

	public Set<NestedTicketDTO> getTickets() {
		return this.tickets;
	}

	public void setTickets(final Set<NestedTicketDTO> tickets) {
		this.tickets = tickets;
	}

	@Override
	public String toString() {
		return String.valueOf(this.id);
	}
}

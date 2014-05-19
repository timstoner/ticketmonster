package com.example.ticketmonster.factory;

import javax.persistence.EntityManager;

import com.example.ticketmonster.dto.BookingDTO;
import com.example.ticketmonster.dto.NestedTicketDTO;
import com.example.ticketmonster.model.Booking;
import com.example.ticketmonster.model.Ticket;

public class BookingFactory extends EntityFactory {

	public static Booking buildEntity(BookingDTO dto, EntityManager em) {
		Booking entity = new Booking();

		// if the nested venue has an id, query the database
		if (dto.getId() != null) {
			entity = findEntity(dto.getId(), em, Booking.class,
					getFindByIdQuery());
		} else {
			entity.setCancellationCode(dto.getCancellationCode());
			entity.setContactEmail(dto.getContactEmail());
			entity.setCreatedOn(dto.getCreatedOn());

			for (NestedTicketDTO ticketDTO : dto.getTickets()) {
				Ticket ticket = TicketFactory.buildEntity(ticketDTO, em);
				entity.getTickets().add(ticket);
			}
		}

		return entity;
	}

	public static String getFindByIdQuery() {
		return "SELECT DISTINCT b FROM Booking b LEFT JOIN FETCH b.tickets LEFT JOIN FETCH b.performance WHERE b.id = :entityId ORDER BY b.id";
	}

	public static String getFindAllQuery() {
		return "SELECT DISTINCT b FROM Booking b LEFT JOIN FETCH b.tickets LEFT JOIN FETCH b.performance ORDER BY b.id";
	}

}

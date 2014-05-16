package com.example.ticketmonster.model.factory;

import javax.persistence.EntityManager;

import com.example.ticketmonster.model.Ticket;
import com.example.ticketmonster.rest.dto.NestedTicketDTO;

public class TicketFactory extends BaseFactory {

	public static Ticket buildEntity(NestedTicketDTO dto, EntityManager em) {
		Ticket entity = new Ticket();

		if (dto.getId() != null) {
			entity = findEntity(dto.getId(), em, Ticket.class,
					getFindByIdQuery());
		} else {
			entity.setPrice(dto.getPrice());
			entity.setSeat(SeatFactory.buildEntity(dto.getSeat(), em));
		}

		return entity;
	}

	public static String getFindByIdQuery() {
		return "SELECT DISTINCT t FROM Ticket t LEFT JOIN FETCH t.ticketCategory WHERE t.id = :entityId ORDER BY t.id";
	}

	public static String getFindAllQuery() {
		return "SELECT DISTINCT t FROM Ticket t LEFT JOIN FETCH t.ticketCategory ORDER BY t.id";
	}
}

package com.example.ticketmonster.factory;

import javax.persistence.EntityManager;

import com.example.ticketmonster.dto.NestedTicketDTO;
import com.example.ticketmonster.dto.TicketDTO;
import com.example.ticketmonster.model.Seat;
import com.example.ticketmonster.model.Ticket;
import com.example.ticketmonster.model.TicketCategory;

public class TicketFactory extends EntityFactory {

	public static Ticket buildEntity(TicketDTO dto, EntityManager em) {
		Ticket entity = new Ticket();

		entity.setId(dto.getId());
		entity.setPrice(dto.getPrice());

		Seat seat = SeatFactory.buildEntity(dto.getSeat(), em);
		entity.setSeat(seat);

		TicketCategory ticketCategory = TicketCategoryFactory.buildEntity(
				dto.getTicketCategory(), em);
		entity.setTicketCategory(ticketCategory);

		return entity;
	}

	public static Ticket buildEntity(NestedTicketDTO dto, EntityManager em) {
		Ticket entity = new Ticket();

		entity = findEntity(dto.getId(), em, Ticket.class, getFindByIdQuery());

		return entity;
	}

	public static String getFindByIdQuery() {
		return "SELECT DISTINCT t FROM Ticket t LEFT JOIN FETCH t.ticketCategory WHERE t.id = :entityId ORDER BY t.id";
	}

	public static String getFindAllQuery() {
		return "SELECT DISTINCT t FROM Ticket t LEFT JOIN FETCH t.ticketCategory ORDER BY t.id";
	}
}

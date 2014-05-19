package com.example.ticketmonster.rest.impl;

import com.example.ticketmonster.dto.TicketDTO;
import com.example.ticketmonster.factory.TicketFactory;
import com.example.ticketmonster.model.Ticket;
import com.example.ticketmonster.rest.TicketService;

public class TicketServiceImpl extends BaseEntityService<Ticket, TicketDTO>
		implements TicketService {

	@Override
	protected Ticket buildEntity(TicketDTO dto) {
		return TicketFactory.buildEntity(dto, em);
	}

	@Override
	protected String getFindAllQuery() {
		return TicketFactory.getFindAllQuery();
	}

	@Override
	protected String getFindByIdQuery() {
		return TicketFactory.getFindByIdQuery();
	}

}
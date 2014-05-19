package com.example.ticketmonster.rest.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.example.ticketmonster.dto.TicketPriceDTO;
import com.example.ticketmonster.factory.TicketPriceFactory;
import com.example.ticketmonster.model.TicketPrice;
import com.example.ticketmonster.rest.TicketPriceService;

public class TicketPriceServiceImpl extends
		BaseEntityService<TicketPrice, TicketPriceDTO> implements
		TicketPriceService {

	private static Logger LOG = LoggerFactory
			.getLogger(TicketPriceServiceImpl.class);

	public TicketPriceServiceImpl() {
		super(TicketPrice.class);
		LOG.debug("Creating Ticket Price Service");
	}

	@Override
	protected TicketPrice buildEntity(TicketPriceDTO dto) {
		return TicketPriceFactory.buildEntity(dto, em);
	}

	@Override
	protected String getFindAllQuery() {
		return TicketPriceFactory.getFindAllQuery();
	}

	@Override
	protected String getFindByIdQuery() {
		return TicketPriceFactory.getFindByIdQuery();
	}

}
package com.example.ticketmonster.rest.impl;

import com.example.ticketmonster.model.TicketPrice;
import com.example.ticketmonster.rest.TicketPriceService;
import com.example.ticketmonster.rest.dto.TicketPriceDTO;

public class TicketPriceServiceImpl extends
		BaseEntityService<TicketPrice, TicketPriceDTO> implements
		TicketPriceService {

	public TicketPriceServiceImpl() {
		super(TicketPrice.class);
	}

}
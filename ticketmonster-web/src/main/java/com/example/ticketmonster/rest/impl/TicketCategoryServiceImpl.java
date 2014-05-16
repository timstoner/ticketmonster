package com.example.ticketmonster.rest.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.example.ticketmonster.model.TicketCategory;
import com.example.ticketmonster.rest.TicketCategoryService;
import com.example.ticketmonster.rest.dto.TicketCategoryDTO;

public class TicketCategoryServiceImpl extends
		BaseEntityService<TicketCategory, TicketCategoryDTO> implements
		TicketCategoryService {
	private static Logger LOG = LoggerFactory
			.getLogger(TicketCategoryServiceImpl.class);

	public TicketCategoryServiceImpl() {
		super(TicketCategory.class);
		LOG.debug("Creating Ticket Category Service");
	}

}
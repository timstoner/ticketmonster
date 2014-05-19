package com.example.ticketmonster.rest.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.example.ticketmonster.dto.TicketCategoryDTO;
import com.example.ticketmonster.factory.TicketCategoryFactory;
import com.example.ticketmonster.model.TicketCategory;
import com.example.ticketmonster.rest.TicketCategoryService;

public class TicketCategoryServiceImpl extends
		BaseEntityService<TicketCategory, TicketCategoryDTO> implements
		TicketCategoryService {
	private static Logger LOG = LoggerFactory
			.getLogger(TicketCategoryServiceImpl.class);

	public TicketCategoryServiceImpl() {
		super(TicketCategory.class);
		LOG.debug("Creating Ticket Category Service");
	}

	@Override
	protected TicketCategory buildEntity(TicketCategoryDTO dto) {
		return TicketCategoryFactory.buildEntity(dto, em);
	}

	@Override
	protected String getFindAllQuery() {
		return TicketCategoryFactory.getFindAllQuery();
	}

	@Override
	protected String getFindByIdQuery() {
		return TicketCategoryFactory.getFindByIdQuery();
	}
}
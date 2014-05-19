package com.example.ticketmonster.rest.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.example.ticketmonster.dto.EventCategoryDTO;
import com.example.ticketmonster.factory.EventCategoryFactory;
import com.example.ticketmonster.model.EventCategory;
import com.example.ticketmonster.rest.EventCategoryService;

public class EventCategoryServiceImpl extends
		BaseEntityService<EventCategory, EventCategoryDTO> implements
		EventCategoryService {

	private static Logger LOG = LoggerFactory
			.getLogger(EventCategoryServiceImpl.class);

	public EventCategoryServiceImpl() {
		super(EventCategory.class);
		LOG.debug("Creating Event Category Service");
	}

	@Override
	protected EventCategory buildEntity(EventCategoryDTO dto) {
		EventCategory entity = EventCategoryFactory.buildEntity(dto,
				getEntityManager());
		return entity;
	}

	@Override
	protected String getFindAllQuery() {
		return EventCategoryFactory.getFindAllQuery();
	}

	@Override
	protected String getFindByIdQuery() {
		return EventCategoryFactory.getFindByIdQuery();
	}

}
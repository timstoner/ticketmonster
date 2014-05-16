package com.example.ticketmonster.rest.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.example.ticketmonster.model.EventCategory;
import com.example.ticketmonster.rest.EventCategoryService;
import com.example.ticketmonster.rest.dto.EventCategoryDTO;

public class EventCategoryServiceImpl extends
		BaseEntityService<EventCategory, EventCategoryDTO> implements
		EventCategoryService {

	private static Logger LOG = LoggerFactory
			.getLogger(EventCategoryServiceImpl.class);

	public EventCategoryServiceImpl() {
		super(EventCategory.class);
		LOG.debug("Creating Event Category Service");
	}

}
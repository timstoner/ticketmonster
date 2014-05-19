package com.example.ticketmonster.rest.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.example.ticketmonster.dto.EventDTO;
import com.example.ticketmonster.factory.EventFactory;
import com.example.ticketmonster.model.Event;
import com.example.ticketmonster.rest.EventService;

/**
 * <p>
 * A JAX-RS endpoint for handling {@link Event}s. Inherits the actual methods
 * from {@link BaseEntityService}, but implements additional search criteria.
 * </p>
 * 
 * @author Marius Bogoevici
 */

public class EventServiceImpl extends BaseEntityService<Event, EventDTO>
		implements EventService {

	private static Logger LOG = LoggerFactory.getLogger(EventServiceImpl.class);

	public EventServiceImpl() {
		super(Event.class);
		LOG.debug("Creating Event Service");
	}

	@Override
	protected Event buildEntity(EventDTO dto) {
		Event entity = EventFactory.buildEntity(dto, getEntityManager());
		return entity;
	}

	@Override
	protected String getFindAllQuery() {
		return EventFactory.getFindAllQuery();
	}

	@Override
	protected String getFindByIdQuery() {
		return EventFactory.getFindByIdQuery();
	}

}

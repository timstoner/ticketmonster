package com.example.ticketmonster.factory;

import javax.persistence.EntityManager;

import com.example.ticketmonster.dto.EventDTO;
import com.example.ticketmonster.dto.NestedEventDTO;
import com.example.ticketmonster.model.Event;
import com.example.ticketmonster.model.EventCategory;
import com.example.ticketmonster.model.MediaItem;

public class EventFactory extends EntityFactory {

	public static Event buildEntity(EventDTO dto, EntityManager em) {
		Event entity;

		entity = new Event();

		entity.setId(dto.getId());
		entity.setName(dto.getName());
		entity.setDescription(dto.getDescription());

		EventCategory ec = EventCategoryFactory.buildEntity(dto.getCategory(),
				em);
		entity.setCategory(ec);

		MediaItem mi = MediaItemFactory.buildEntity(dto.getMediaItem(), em);
		entity.setMediaItem(mi);

		return entity;
	}

	public static Event buildEntity(NestedEventDTO dto, EntityManager em) {
		Event entity;

		entity = findEntity(dto.getId(), em, Event.class, getFindByIdQuery());

		return entity;
	}

	public static String getFindByIdQuery() {
		return "SELECT DISTINCT e FROM Event e LEFT JOIN FETCH e.mediaItem LEFT JOIN FETCH e.category WHERE e.id = :entityId ORDER BY e.id";
	}

	public static String getFindAllQuery() {
		return "SELECT DISTINCT e FROM Event e LEFT JOIN FETCH e.mediaItem LEFT JOIN FETCH e.category ORDER BY e.id";
	}

}

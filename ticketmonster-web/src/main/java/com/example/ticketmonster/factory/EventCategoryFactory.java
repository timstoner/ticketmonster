package com.example.ticketmonster.factory;

import javax.persistence.EntityManager;

import com.example.ticketmonster.dto.EventCategoryDTO;
import com.example.ticketmonster.dto.NestedEventCategoryDTO;
import com.example.ticketmonster.model.EventCategory;

public class EventCategoryFactory extends EntityFactory {

	public static EventCategory buildEntity(EventCategoryDTO dto,
			EntityManager em) {
		EventCategory entity = new EventCategory();

		entity.setId(dto.getId());
		entity.setDescription(dto.getDescription());

		return entity;
	}

	public static EventCategory buildEntity(NestedEventCategoryDTO dto,
			EntityManager em) {
		EventCategory entity;

		entity = findEntity(dto.getId(), em, EventCategory.class,
				getFindByIdQuery());

		return entity;
	}

	public static String getFindAllQuery() {
		return "SELECT DISTINCT e FROM EventCategory e ORDER BY e.id";
	}

	public static String getFindByIdQuery() {
		return "SELECT DISTINCT e FROM EventCategory e WHERE e.id = :entityId ORDER BY e.id";
	}
}

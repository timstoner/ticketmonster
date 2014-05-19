package com.example.ticketmonster.factory;

import javax.persistence.EntityManager;

import com.example.ticketmonster.dto.NestedTicketCategoryDTO;
import com.example.ticketmonster.dto.TicketCategoryDTO;
import com.example.ticketmonster.model.TicketCategory;

public class TicketCategoryFactory extends EntityFactory {

	public static TicketCategory buildEntity(TicketCategoryDTO dto,
			EntityManager em) {
		TicketCategory entity = new TicketCategory();

		entity.setId(dto.getId());
		entity.setDescription(dto.getDescription());

		return null;
	}

	public static TicketCategory buildEntity(NestedTicketCategoryDTO dto,
			EntityManager em) {
		TicketCategory entity;

		entity = findEntity(dto.getId(), em, TicketCategory.class,
				getFindByIdQuery());

		return entity;
	}

	public static String getFindByIdQuery() {
		return "SELECT DISTINCT t FROM TicketCategory t WHERE t.id = :entityId ORDER BY t.id";
	}

	public static String getFindAllQuery() {
		return "SELECT DISTINCT t FROM TicketCategory t ORDER BY t.id";
	}

}

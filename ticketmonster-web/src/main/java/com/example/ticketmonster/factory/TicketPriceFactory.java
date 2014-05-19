package com.example.ticketmonster.factory;

import javax.persistence.EntityManager;

import com.example.ticketmonster.dto.NestedTicketPriceDTO;
import com.example.ticketmonster.dto.TicketPriceDTO;
import com.example.ticketmonster.model.Section;
import com.example.ticketmonster.model.Show;
import com.example.ticketmonster.model.TicketCategory;
import com.example.ticketmonster.model.TicketPrice;

public class TicketPriceFactory extends EntityFactory {

	public static TicketPrice buildEntity(TicketPriceDTO dto, EntityManager em) {
		TicketPrice entity = new TicketPrice();

		entity.setId(dto.getId());
		entity.setPrice(dto.getPrice());

		Section section = SectionFactory.buildSection(dto.getSection(), em);
		entity.setSection(section);

		Show show = ShowFactory.buildEntity(dto.getShow(), em);
		entity.setShow(show);

		TicketCategory ticketCategory = TicketCategoryFactory.buildEntity(
				dto.getTicketCategory(), em);
		entity.setTicketCategory(ticketCategory);

		return entity;
	}

	public static TicketPrice buildEntity(NestedTicketPriceDTO dto,
			EntityManager em) {
		TicketPrice entity;

		entity = findEntity(dto.getId(), em, TicketPrice.class,
				getFindByIdQuery());

		return entity;
	}

	public static String getFindByIdQuery() {
		return "SELECT DISTINCT t FROM TicketPrice t LEFT JOIN FETCH t.show LEFT JOIN FETCH t.section LEFT JOIN FETCH t.ticketCategory WHERE t.id = :entityId ORDER BY t.id";
	}

	public static String getFindAllQuery() {
		return "SELECT DISTINCT t FROM TicketPrice t LEFT JOIN FETCH t.show LEFT JOIN FETCH t.section LEFT JOIN FETCH t.ticketCategory ORDER BY t.id";
	}
}

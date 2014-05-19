package com.example.ticketmonster.factory;

import javax.persistence.EntityManager;

import com.example.ticketmonster.dto.NestedSectionDTO;
import com.example.ticketmonster.dto.SectionDTO;
import com.example.ticketmonster.model.Section;
import com.example.ticketmonster.model.Venue;

public class SectionFactory extends EntityFactory {

	public static Section buildSection(SectionDTO dto, EntityManager em) {
		Section entity = new Section();

		entity.setId(dto.getId());
		entity.setDescription(dto.getDescription());
		entity.setRowCapacity(dto.getRowCapacity());
		entity.setName(dto.getName());
		entity.setNumberOfRows(dto.getNumberOfRows());

		Venue venue = VenueFactory.buildEntity(dto.getVenue(), em);
		entity.setVenue(venue);

		return entity;
	}

	public static Section buildSection(NestedSectionDTO dto, EntityManager em) {
		Section entity;

		entity = findEntity(dto.getId(), em, Section.class, getFindByIdQuery());

		return entity;
	}

	public static String getFindByIdQuery() {
		return "SELECT DISTINCT s FROM Section s LEFT JOIN FETCH s.venue WHERE s.id = :entityId ORDER BY s.id";
	}

	public static String getFindAllQuery() {
		return "SELECT DISTINCT s FROM Section s LEFT JOIN FETCH s.venue ORDER BY s.id";
	}

}

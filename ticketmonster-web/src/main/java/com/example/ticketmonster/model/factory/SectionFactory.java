package com.example.ticketmonster.model.factory;

import javax.persistence.EntityManager;

import com.example.ticketmonster.model.Section;
import com.example.ticketmonster.model.Venue;
import com.example.ticketmonster.rest.dto.NestedSectionDTO;
import com.example.ticketmonster.rest.dto.SectionDTO;

public class SectionFactory extends BaseFactory {

	public static Section buildSection(SectionDTO dto, EntityManager em) {
		Section entity = new Section();

		// if the nested venue has an id, query the database
		if (dto.getId() != null) {
			entity = findEntity(dto.getId(), em, Section.class,
					getFindByIdQuery());
		} else {
			entity.setDescription(dto.getDescription());
			entity.setRowCapacity(dto.getRowCapacity());
			entity.setName(dto.getName());
			entity.setNumberOfRows(dto.getNumberOfRows());

			if (dto.getVenue() != null) {
				Venue venue = VenueFactory.buildVenue(dto.getVenue(), em);
				entity.setVenue(venue);
			}
		}

		return entity;
	}

	public static Section buildSection(NestedSectionDTO dto, EntityManager em) {
		Section entity = new Section();

		// if the nested section has an id, query the database
		if (dto.getId() != null) {
			entity = findEntity(dto.getId(), em, Section.class,
					getFindByIdQuery());
		} else {
			entity.setDescription(dto.getDescription());
			entity.setRowCapacity(dto.getRowCapacity());
			entity.setName(dto.getName());
			entity.setNumberOfRows(dto.getNumberOfRows());
		}

		return entity;
	}

	public static String getFindByIdQuery() {
		return "SELECT DISTINCT s FROM Section s LEFT JOIN FETCH s.venue WHERE s.id = :entityId ORDER BY s.id";
	}

	public static String getFindAllQuery() {
		return "SELECT DISTINCT s FROM Section s LEFT JOIN FETCH s.venue ORDER BY s.id";
	}

}

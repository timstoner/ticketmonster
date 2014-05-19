package com.example.ticketmonster.factory;

import javax.persistence.EntityManager;

import com.example.ticketmonster.dto.NestedSectionDTO;
import com.example.ticketmonster.dto.NestedVenueDTO;
import com.example.ticketmonster.dto.VenueDTO;
import com.example.ticketmonster.model.Address;
import com.example.ticketmonster.model.MediaItem;
import com.example.ticketmonster.model.Section;
import com.example.ticketmonster.model.Venue;

public class VenueFactory extends EntityFactory {

	public static Venue buildEntity(VenueDTO dto, EntityManager em) {
		Venue entity;

		entity = new Venue();
		entity.setAddress(Address.buildEntity(dto.getAddress()));
		entity.setCapacity(dto.getCapacity());
		entity.setDescription(dto.getDescription());

		MediaItem mi = MediaItemFactory.buildEntity(dto.getMediaItem(), em);
		entity.setMediaItem(mi);
		entity.setName(dto.getName());

		for (NestedSectionDTO secDTO : dto.getSections()) {
			Section section = SectionFactory.buildSection(secDTO, em);
			entity.getSections().add(section);
		}

		return entity;
	}

	public static Venue buildEntity(NestedVenueDTO dto, EntityManager em) {
		Venue entity;

		entity = findEntity(dto.getId(), em, Venue.class, getFindByIdQuery());

		return entity;
	}

	public static String getFindByIdQuery() {
		return "SELECT DISTINCT v FROM Venue v LEFT JOIN FETCH v.sections LEFT JOIN FETCH v.mediaItem WHERE v.id = :entityId ORDER BY v.id";
	}

	public static String getFindAllQuery() {
		return "SELECT DISTINCT v FROM Venue v LEFT JOIN FETCH v.sections LEFT JOIN FETCH v.mediaItem ORDER BY v.id";
	}
}

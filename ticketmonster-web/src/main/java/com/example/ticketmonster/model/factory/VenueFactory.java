package com.example.ticketmonster.model.factory;

import javax.persistence.EntityManager;

import com.example.ticketmonster.model.Address;
import com.example.ticketmonster.model.MediaItem;
import com.example.ticketmonster.model.Section;
import com.example.ticketmonster.model.Venue;
import com.example.ticketmonster.rest.dto.NestedSectionDTO;
import com.example.ticketmonster.rest.dto.NestedVenueDTO;
import com.example.ticketmonster.rest.dto.VenueDTO;

public class VenueFactory extends BaseFactory {

	public static Venue buildVenue(VenueDTO dto, EntityManager em) {
		Venue entity;

		// lookup venue in database
		entity = findEntity(dto.getId(), em, Venue.class, getFindByIdQuery());
		// if no venue in the database, create a new one
		if (entity == null) {
			entity = new Venue();
			entity.setAddress(Address.buildEntity(dto.getAddress()));
			entity.setCapacity(dto.getCapacity());
			entity.setDescription(dto.getDescription());
			entity.setMediaItem(MediaItem.buildMediaItem(dto.getMediaItem(), em));
			entity.setName(dto.getName());

			for (NestedSectionDTO secDTO : dto.getSections()) {
				Section section = SectionFactory.buildSection(secDTO, em);
				entity.getSections().add(section);
			}
		}

		return entity;
	}

	public static Venue buildVenue(NestedVenueDTO dto, EntityManager em) {
		Venue entity = new Venue();

		entity = findEntity(dto.getId(), em, Venue.class, getFindByIdQuery());

		if (entity == null) {
			entity = new Venue();
			entity.setDescription(dto.getDescription());
			entity.setName(dto.getName());
			entity.setCapacity(dto.getCapacity());

			if (dto.getAddress() != null) {
				Address address = Address.buildEntity(dto.getAddress());
				entity.setAddress(address);
			}
		}

		return entity;
	}

	public static String getFindByIdQuery() {
		return "SELECT DISTINCT v FROM Venue v LEFT JOIN FETCH v.sections LEFT JOIN FETCH v.mediaItem WHERE v.id = :entityId ORDER BY v.id";
	}

	public static String getFindAllQuery() {
		return "SELECT DISTINCT v FROM Venue v LEFT JOIN FETCH v.sections LEFT JOIN FETCH v.mediaItem ORDER BY v.id";
	}
}

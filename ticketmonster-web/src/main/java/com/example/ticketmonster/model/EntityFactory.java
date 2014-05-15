package com.example.ticketmonster.model;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import com.example.ticketmonster.rest.dto.AddressDTO;
import com.example.ticketmonster.rest.dto.NestedVenueDTO;
import com.example.ticketmonster.rest.dto.SectionDTO;

public class EntityFactory {

	@PersistenceContext
	private static EntityManager em;

	public static Address buildAddress(AddressDTO dto) {
		Address entity = new Address();

		entity.setStreet(dto.getStreet());
		entity.setCountry(dto.getCountry());
		entity.setCity(dto.getCity());

		return entity;
	}

	public static Venue buildVenue(NestedVenueDTO dto) {
		Venue entity = new Venue();

		// if the nested venue has an id, query the database
		if (dto.getId() != null) {
			TypedQuery<Venue> findByIdQuery = em.createQuery(
					"SELECT DISTINCT v FROM Venue v WHERE v.id = :entityId",
					Venue.class);
			findByIdQuery.setParameter("entityId", dto.getId());
			try {
				entity = findByIdQuery.getSingleResult();
			} catch (NoResultException nre) {
				entity = null;
			}
		} else {
			if (dto.getAddress() != null) {
				Address address = buildAddress(dto.getAddress());
				entity.setAddress(address);
			}
			entity.setDescription(dto.getDescription());
			entity.setName(dto.getName());
			entity.setCapacity(dto.getCapacity());
		}

		return entity;
	}

	public static Section buildSection(SectionDTO dto) {
		Section entity = new Section();

		entity.setDescription(dto.getDescription());
		entity.setRowCapacity(dto.getRowCapacity());
		entity.setName(dto.getName());
		entity.setNumberOfRows(dto.getNumberOfRows());

		if (dto.getVenue() != null) {
			Venue venue = buildVenue(dto.getVenue());
			entity.setVenue(venue);
		}

		return entity;
	}

}

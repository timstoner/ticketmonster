package com.example.ticketmonster.rest.dto;

import java.io.IOException;
import java.io.Serializable;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.example.ticketmonster.model.Venue;

public class NestedVenueDTO  extends BaseDTO implements Serializable {

	private static final Logger LOG = LoggerFactory
			.getLogger(NestedVenueDTO.class);
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Long id;
	private AddressDTO address;
	private String description;
	private String name;
	private int capacity;

	public NestedVenueDTO() {
	}

	public NestedVenueDTO(final Venue entity) {
		if (entity != null) {
			this.id = entity.getId();
			this.address = new AddressDTO(entity.getAddress());
			this.description = entity.getDescription();
			this.name = entity.getName();
			this.capacity = entity.getCapacity();
		}
	}

	public Venue fromDTO(Venue entity, EntityManager em) {
		if (entity == null) {
			entity = new Venue();
		}
		if (this.id != null) {
			TypedQuery<Venue> findByIdQuery = em.createQuery(
					"SELECT DISTINCT v FROM Venue v WHERE v.id = :entityId",
					Venue.class);
			findByIdQuery.setParameter("entityId", this.id);
			try {
				entity = findByIdQuery.getSingleResult();
			} catch (javax.persistence.NoResultException nre) {
				entity = null;
			}
			return entity;
		}
		if (this.address != null) {
			entity.setAddress(this.address.fromDTO(entity.getAddress(), em));
		}
		entity.setDescription(this.description);
		entity.setName(this.name);
		entity.setCapacity(this.capacity);
		entity = em.merge(entity);
		return entity;
	}

	public Long getId() {
		return this.id;
	}

	public void setId(final Long id) {
		this.id = id;
	}

	public AddressDTO getAddress() {
		return this.address;
	}

	public void setAddress(final AddressDTO address) {
		this.address = address;
	}

	public String getDescription() {
		return this.description;
	}

	public void setDescription(final String description) {
		this.description = description;
	}

	public String getName() {
		return this.name;
	}

	public void setName(final String name) {
		this.name = name;
	}

	public int getCapacity() {
		return this.capacity;
	}

	public void setCapacity(final int capacity) {
		this.capacity = capacity;
	}

	public boolean equals(Object obj) {
		return EqualsBuilder.reflectionEquals(this, obj);
	}

	public int hashCode() {
		return HashCodeBuilder.reflectionHashCode(this);
	}

	public static NestedVenueDTO newInstance(String entity) {
		ObjectMapper mapper = new ObjectMapper();
		NestedVenueDTO dto = null;
		try {
			dto = mapper.readValue(entity, NestedVenueDTO.class);
		} catch (IOException e) {
			LOG.warn("Problem building address DTO from JSON", e);
		}
		return dto;
	}
}
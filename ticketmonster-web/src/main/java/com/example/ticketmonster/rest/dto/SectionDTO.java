package com.example.ticketmonster.rest.dto;

import java.io.IOException;
import java.io.Serializable;

import javax.persistence.EntityManager;
import javax.xml.bind.annotation.XmlRootElement;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.example.ticketmonster.model.Section;

@XmlRootElement
public class SectionDTO implements Serializable {
	private static final Logger LOG = LoggerFactory.getLogger(SectionDTO.class);

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Long id;
	private String description;
	private int rowCapacity;
	private String name;
	private int capacity;
	private int numberOfRows;
	private NestedVenueDTO venue;

	public SectionDTO() {
	}

	public SectionDTO(final Section entity) {
		if (entity != null) {
			this.id = entity.getId();
			this.description = entity.getDescription();
			this.rowCapacity = entity.getRowCapacity();
			this.name = entity.getName();
			this.capacity = entity.getCapacity();
			this.numberOfRows = entity.getNumberOfRows();
			this.venue = new NestedVenueDTO(entity.getVenue());
		}
	}

	public Section fromDTO(Section entity, EntityManager em) {
		if (entity == null) {
			entity = new Section();
		}
		entity.setDescription(this.description);
		entity.setRowCapacity(this.rowCapacity);
		entity.setName(this.name);
		entity.setNumberOfRows(this.numberOfRows);
		if (this.venue != null) {
			entity.setVenue(this.venue.fromDTO(entity.getVenue(), em));
		}
		entity = em.merge(entity);
		return entity;
	}

	public Long getId() {
		return this.id;
	}

	public void setId(final Long id) {
		this.id = id;
	}

	public String getDescription() {
		return this.description;
	}

	public void setDescription(final String description) {
		this.description = description;
	}

	public int getRowCapacity() {
		return this.rowCapacity;
	}

	public void setRowCapacity(final int rowCapacity) {
		this.rowCapacity = rowCapacity;
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

	public int getNumberOfRows() {
		return this.numberOfRows;
	}

	public void setNumberOfRows(final int numberOfRows) {
		this.numberOfRows = numberOfRows;
	}

	public NestedVenueDTO getVenue() {
		return this.venue;
	}

	public void setVenue(final NestedVenueDTO venue) {
		this.venue = venue;
	}

	public boolean equals(Object obj) {
		return EqualsBuilder.reflectionEquals(this, obj);
	}

	public int hashCode() {
		return HashCodeBuilder.reflectionHashCode(this);
	}

	public static SectionDTO newInstance(String entity) {
		ObjectMapper mapper = new ObjectMapper();
		SectionDTO dto = null;
		try {
			dto = mapper.readValue(entity, SectionDTO.class);
		} catch (IOException e) {
			LOG.warn("Problem building address DTO from JSON", e);
		}
		return dto;

	}
}
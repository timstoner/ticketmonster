package com.example.ticketmonster.rest.dto;

import java.io.Serializable;

import com.example.ticketmonster.model.EventCategory;

import javax.persistence.EntityManager;
import javax.xml.bind.annotation.XmlRootElement;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

@XmlRootElement
public class EventCategoryDTO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Long id;
	private String description;

	public EventCategoryDTO() {
	}

	public EventCategoryDTO(final EventCategory entity) {
		if (entity != null) {
			this.id = entity.getId();
			this.description = entity.getDescription();
		}
	}

	public EventCategory fromDTO(EventCategory entity, EntityManager em) {
		if (entity == null) {
			entity = new EventCategory();
		}

		entity.setDescription(this.description);
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

	public boolean equals(Object obj) {
		return EqualsBuilder.reflectionEquals(this, obj);
	}

	public int hashCode() {
		return HashCodeBuilder.reflectionHashCode(this);
	}
}
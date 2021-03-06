package com.example.ticketmonster.dto;

import java.io.Serializable;

import com.example.ticketmonster.model.Performance;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import java.util.Date;

public class NestedPerformanceDTO extends BaseDTO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Long id;
	private Date date;
	private String displayTitle;

	public NestedPerformanceDTO() {
	}

	public NestedPerformanceDTO(final Performance entity) {
		if (entity != null) {
			this.id = entity.getId();
			this.date = entity.getDate();
			this.displayTitle = entity.toString();
		}
	}

	public Performance fromDTO(Performance entity, EntityManager em) {
		if (entity == null) {
			entity = new Performance();
		}
		if (this.id != null) {
			TypedQuery<Performance> findByIdQuery = em
					.createQuery(
							"SELECT DISTINCT p FROM Performance p WHERE p.id = :entityId",
							Performance.class);
			findByIdQuery.setParameter("entityId", this.id);
			try {
				entity = findByIdQuery.getSingleResult();
			} catch (javax.persistence.NoResultException nre) {
				entity = null;
			}
			return entity;
		}
		entity.setDate(this.date);
		entity = em.merge(entity);
		return entity;
	}

	public Long getId() {
		return this.id;
	}

	public void setId(final Long id) {
		this.id = id;
	}

	public Date getDate() {
		return this.date;
	}

	public void setDate(final Date date) {
		this.date = date;
	}

	public String getDisplayTitle() {
		return this.displayTitle;
	}
}

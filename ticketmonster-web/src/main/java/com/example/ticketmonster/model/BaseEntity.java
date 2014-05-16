package com.example.ticketmonster.model;

import javax.persistence.EntityManager;

public abstract class BaseEntity<T> implements Identifiable {

	public abstract Long getId();

	public abstract T convertToDTO();

	// public abstract String getFindByIdQuery();
	//
	// public abstract String getFindAllQuery();
	//
}

package com.example.ticketmonster.model;

public abstract class BaseEntity<T> implements Identifiable {

	@Override
	public abstract Long getId();

	public abstract T buildDTO();

}

package com.example.ticketmonster.model;

public abstract class BaseEntity<T> implements Identifiable {

	public abstract Long getId();

	public abstract T buildDTO();

}

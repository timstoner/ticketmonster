package com.example.ticketmonster.model;

import javax.persistence.EntityManager;

public interface Persistable<T> {

	public T toEntity(T t, EntityManager em);

}

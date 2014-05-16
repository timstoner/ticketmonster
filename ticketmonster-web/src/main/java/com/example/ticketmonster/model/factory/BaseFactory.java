package com.example.ticketmonster.model.factory;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;

public class BaseFactory {
	public static <T> T findEntity(long id, EntityManager em,
			Class<T> entityClass, String query) {

		T entity;
		TypedQuery<T> findByIdQuery = em.createQuery(query, entityClass);
		findByIdQuery.setParameter("entityId", id);

		try {
			entity = findByIdQuery.getSingleResult();
		} catch (NoResultException nre) {
			entity = null;
		}

		return entity;
	}
}

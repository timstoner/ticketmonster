package com.example.ticketmonster.factory;

import java.io.IOException;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;

import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EntityFactory {

	private static Logger LOG = LoggerFactory.getLogger(EntityFactory.class);

	private static ObjectMapper mapper = new ObjectMapper();

	public static <T> T findEntity(long id, EntityManager em,
			Class<T> entityClass, String query) {

		T entity;
		TypedQuery<T> findByIdQuery = em.createQuery(query, entityClass);
		findByIdQuery.setParameter("entityId", id);

		try {
			entity = findByIdQuery.getSingleResult();
		} catch (NoResultException nre) {
			LOG.warn("no entity found for: {} {}", entityClass, id);
			entity = null;
		}

		return entity;
	}

	public static <T> T build(Class<T> clas, String content) {
		T dto = null;
		try {
			dto = mapper.readValue(content, clas);
		} catch (IOException e) {
			LOG.warn("Problem building DTO from JSON", e);
		}
		return dto;
	}
}

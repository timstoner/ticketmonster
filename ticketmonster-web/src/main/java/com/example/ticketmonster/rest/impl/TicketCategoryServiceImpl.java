package com.example.ticketmonster.rest.impl;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.UriBuilder;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.example.ticketmonster.model.TicketCategory;
import com.example.ticketmonster.rest.TicketCategoryService;
import com.example.ticketmonster.rest.dto.TicketCategoryDTO;

public class TicketCategoryServiceImpl implements TicketCategoryService {
	private static Logger LOG = LoggerFactory
			.getLogger(TicketCategoryServiceImpl.class);

	@PersistenceContext
	private EntityManager em;

	public Response create(TicketCategoryDTO dto) {
		LOG.debug("create {}", dto.getId());

		TicketCategory entity = dto.fromDTO(null, em);
		em.persist(entity);
		return Response.created(
				UriBuilder.fromResource(TicketCategoryServiceImpl.class)
						.path(String.valueOf(entity.getId())).build()).build();
	}

	public Response deleteById(Long id) {
		LOG.debug("deleteById {}", id);

		TicketCategory entity = em.find(TicketCategory.class, id);
		if (entity == null) {
			return Response.status(Status.NOT_FOUND).build();
		}
		em.remove(entity);
		return Response.noContent().build();
	}

	public Response findById(Long id) {
		LOG.debug("findById {}", id);

		TypedQuery<TicketCategory> findByIdQuery = em
				.createQuery(
						"SELECT DISTINCT t FROM TicketCategory t WHERE t.id = :entityId ORDER BY t.id",
						TicketCategory.class);
		findByIdQuery.setParameter("entityId", id);
		TicketCategory entity;
		try {
			entity = findByIdQuery.getSingleResult();
		} catch (NoResultException nre) {
			entity = null;
		}
		if (entity == null) {
			return Response.status(Status.NOT_FOUND).build();
		}
		TicketCategoryDTO dto = new TicketCategoryDTO(entity);
		return Response.ok(dto).build();
	}

	public List<TicketCategoryDTO> listAll() {
		LOG.debug("listAll");

		final List<TicketCategory> searchResults = em.createQuery(
				"SELECT DISTINCT t FROM TicketCategory t ORDER BY t.id",
				TicketCategory.class).getResultList();
		final List<TicketCategoryDTO> results = new ArrayList<TicketCategoryDTO>();
		for (TicketCategory searchResult : searchResults) {
			TicketCategoryDTO dto = new TicketCategoryDTO(searchResult);
			results.add(dto);
		}
		return results;
	}

	public Response update(Long id, TicketCategoryDTO dto) {
		LOG.debug("update {}", id);

		TypedQuery<TicketCategory> findByIdQuery = em
				.createQuery(
						"SELECT DISTINCT t FROM TicketCategory t WHERE t.id = :entityId ORDER BY t.id",
						TicketCategory.class);
		findByIdQuery.setParameter("entityId", id);
		TicketCategory entity;
		try {
			entity = findByIdQuery.getSingleResult();
		} catch (NoResultException nre) {
			entity = null;
		}
		entity = dto.fromDTO(entity, em);
		entity = em.merge(entity);
		return Response.noContent().build();
	}
}
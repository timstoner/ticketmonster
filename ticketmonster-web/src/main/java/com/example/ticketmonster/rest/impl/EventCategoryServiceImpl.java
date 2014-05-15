package com.example.ticketmonster.rest.impl;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.UriBuilder;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.example.ticketmonster.model.EventCategory;
import com.example.ticketmonster.rest.dto.EventCategoryDTO;

@Path("/eventcategories")
public class EventCategoryServiceImpl {

	private static Logger LOG = LoggerFactory
			.getLogger(EventCategoryServiceImpl.class);

	@PersistenceContext
	private EntityManager em;

	@POST
	@Consumes("application/json")
	public Response create(EventCategoryDTO dto) {
		LOG.debug("create {} ", dto.getId());

		EventCategory entity = EventCategory.buildEntity(dto);
		em.persist(entity);
		return Response.created(
				UriBuilder.fromResource(EventCategoryServiceImpl.class)
						.path(String.valueOf(entity.getId())).build()).build();
	}

	@DELETE
	@Path("/{id:[0-9][0-9]*}")
	public Response deleteById(@PathParam("id") Long id) {
		LOG.debug("deleteById {}", id);

		EventCategory entity = em.find(EventCategory.class, id);
		if (entity == null) {
			return Response.status(Status.NOT_FOUND).build();
		}
		em.remove(entity);
		return Response.noContent().build();
	}

	@GET
	@Path("/{id:[0-9][0-9]*}")
	@Produces("application/json")
	public Response findById(@PathParam("id") Long id) {
		LOG.debug("findById {}", id);

		TypedQuery<EventCategory> findByIdQuery = em
				.createQuery(
						"SELECT DISTINCT e FROM EventCategory e WHERE e.id = :entityId ORDER BY e.id",
						EventCategory.class);
		findByIdQuery.setParameter("entityId", id);
		EventCategory entity;
		try {
			entity = findByIdQuery.getSingleResult();
		} catch (NoResultException nre) {
			entity = null;
		}
		if (entity == null) {
			return Response.status(Status.NOT_FOUND).build();
		}
		EventCategoryDTO dto = entity.buildDTO();
		return Response.ok(dto).build();
	}

	@GET
	@Produces("application/json")
	public List<EventCategoryDTO> listAll() {
		LOG.debug("listAll");

		final List<EventCategory> searchResults = em.createQuery(
				"SELECT DISTINCT e FROM EventCategory e ORDER BY e.id",
				EventCategory.class).getResultList();
		final List<EventCategoryDTO> results = new ArrayList<EventCategoryDTO>();
		for (EventCategory searchResult : searchResults) {
			EventCategoryDTO dto = searchResult.buildDTO();
			results.add(dto);
		}
		return results;
	}

	@PUT
	@Path("/{id:[0-9][0-9]*}")
	@Consumes("application/json")
	public Response update(@PathParam("id") Long id, EventCategoryDTO dto) {
		LOG.debug("update {}", id);

		TypedQuery<EventCategory> findByIdQuery = em.createQuery(
				getFindByIdQuery(), EventCategory.class);

		findByIdQuery.setParameter("entityId", id);

		EventCategory entity;
		try {
			entity = findByIdQuery.getSingleResult();
		} catch (NoResultException nre) {
			entity = null;
		}
		entity = EventCategory.buildEntity(dto);
		entity = em.merge(entity);
		return Response.noContent().build();
	}

	protected String getFindByIdQuery() {
		return "SELECT DISTINCT e FROM EventCategory e WHERE e.id = :entityId ORDER BY e.id";
	}
}
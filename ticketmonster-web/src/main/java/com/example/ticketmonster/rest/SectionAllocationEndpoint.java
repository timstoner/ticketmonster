package com.example.ticketmonster.rest;

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

import com.example.ticketmonster.model.SectionAllocation;
import com.example.ticketmonster.rest.dto.SectionAllocationDTO;

/**
 * 
 */
@Path("/sectionallocations")
public class SectionAllocationEndpoint {

	private static final Logger LOG = LoggerFactory
			.getLogger(SectionAllocationEndpoint.class);

	@PersistenceContext
	private EntityManager em;

	@POST
	@Consumes("application/json")
	public Response create(SectionAllocationDTO dto) {
		LOG.debug("create");
		
		SectionAllocation entity = dto.fromDTO(null, em);
		em.persist(entity);
		return Response.created(
				UriBuilder.fromResource(SectionAllocationEndpoint.class)
						.path(String.valueOf(entity.getId())).build()).build();
	}

	@DELETE
	@Path("/{id:[0-9][0-9]*}")
	public Response deleteById(@PathParam("id") Long id) {
		LOG.debug("deleteById {}", id);
		SectionAllocation entity = em.find(SectionAllocation.class, id);
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
		TypedQuery<SectionAllocation> findByIdQuery = em
				.createQuery(
						"SELECT DISTINCT s FROM SectionAllocation s LEFT JOIN FETCH s.performance LEFT JOIN FETCH s.section WHERE s.id = :entityId ORDER BY s.id",
						SectionAllocation.class);
		findByIdQuery.setParameter("entityId", id);
		SectionAllocation entity;
		try {
			entity = findByIdQuery.getSingleResult();
		} catch (NoResultException nre) {
			entity = null;
		}
		if (entity == null) {
			return Response.status(Status.NOT_FOUND).build();
		}
		SectionAllocationDTO dto = new SectionAllocationDTO(entity);
		return Response.ok(dto).build();
	}

	@GET
	@Produces("application/json")
	public List<SectionAllocationDTO> listAll() {
		LOG.debug("listAll");
		final List<SectionAllocation> searchResults = em
				.createQuery(
						"SELECT DISTINCT s FROM SectionAllocation s LEFT JOIN FETCH s.performance LEFT JOIN FETCH s.section ORDER BY s.id",
						SectionAllocation.class).getResultList();
		final List<SectionAllocationDTO> results = new ArrayList<SectionAllocationDTO>();
		for (SectionAllocation searchResult : searchResults) {
			SectionAllocationDTO dto = new SectionAllocationDTO(searchResult);
			results.add(dto);
		}
		return results;
	}

	@PUT
	@Path("/{id:[0-9][0-9]*}")
	@Consumes("application/json")
	public Response update(@PathParam("id") Long id, SectionAllocationDTO dto) {
		LOG.debug("update {}", id);
		TypedQuery<SectionAllocation> findByIdQuery = em
				.createQuery(
						"SELECT DISTINCT s FROM SectionAllocation s LEFT JOIN FETCH s.performance LEFT JOIN FETCH s.section WHERE s.id = :entityId ORDER BY s.id",
						SectionAllocation.class);
		findByIdQuery.setParameter("entityId", id);
		SectionAllocation entity;
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
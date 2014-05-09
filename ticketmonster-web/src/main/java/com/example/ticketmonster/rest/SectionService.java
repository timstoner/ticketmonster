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

import com.example.ticketmonster.model.Section;
import com.example.ticketmonster.rest.dto.SectionDTO;

@Path("/sections")
public class SectionService extends BaseEntityService<Section, SectionDTO> {

	private static Logger LOG = LoggerFactory.getLogger(SectionService.class);

	public SectionService() {
		super(Section.class, SectionDTO.class);
	}

	@PersistenceContext
	private EntityManager em;

	@POST
	@Consumes("application/json")
	public Response create(SectionDTO dto) {
		LOG.debug("create {}", dto.getName());

		Section entity = dto.fromDTO(null, em);
		em.persist(entity);
		return Response.created(
				UriBuilder.fromResource(SectionService.class)
						.path(String.valueOf(entity.getId())).build()).build();
	}

	@DELETE
	@Path("/{id:[0-9][0-9]*}")
	public Response deleteById(@PathParam("id") Long id) {
		LOG.debug("deleteById {}", id);

		Section entity = em.find(Section.class, id);
		if (entity == null) {
			return Response.status(Status.NOT_FOUND).build();
		}
		em.remove(entity);
		return Response.noContent().build();
	}

	@GET
	@Produces("application/json")
	public List<SectionDTO> listAll() {
		LOG.debug("listAll");
		final List<Section> searchResults = em
				.createQuery(
						"SELECT DISTINCT s FROM Section s LEFT JOIN FETCH s.venue ORDER BY s.id",
						Section.class).getResultList();
		final List<SectionDTO> results = new ArrayList<SectionDTO>();
		for (Section searchResult : searchResults) {
			SectionDTO dto = new SectionDTO(searchResult);
			results.add(dto);
		}
		return results;
	}

	@PUT
	@Path("/{id:[0-9][0-9]*}")
	@Consumes("application/json")
	public Response update(@PathParam("id") Long id, SectionDTO dto) {
		LOG.debug("update {}", id);

		TypedQuery<Section> findByIdQuery = em
				.createQuery(
						"SELECT DISTINCT s FROM Section s LEFT JOIN FETCH s.venue WHERE s.id = :entityId ORDER BY s.id",
						Section.class);
		findByIdQuery.setParameter("entityId", id);
		Section entity;
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
package com.example.ticketmonster.rest.impl;

import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;
import javax.ws.rs.Consumes;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.UriBuilder;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.example.ticketmonster.model.Section;
import com.example.ticketmonster.rest.BaseEntityService;
import com.example.ticketmonster.rest.SectionService;
import com.example.ticketmonster.rest.dto.SectionDTO;

@Path("/sections")
public class SectionServiceImpl extends BaseEntityService<Section> implements
		SectionService {

	private static Logger LOG = LoggerFactory
			.getLogger(SectionServiceImpl.class);

	public SectionServiceImpl() {
		super(Section.class);
	}

	public Response create(SectionDTO dto) {
		LOG.debug("create {}", dto.getName());

		Section entity = dto.fromDTO(null, getEntityManager());
		getEntityManager().persist(entity);
		return Response.created(
				UriBuilder.fromResource(SectionServiceImpl.class)
						.path(String.valueOf(entity.getId())).build()).build();
	}

	public Response deleteById(Long id) {
		LOG.debug("deleteById {}", id);

		Section entity = getEntityManager().find(Section.class, id);
		if (entity == null) {
			return Response.status(Status.NOT_FOUND).build();
		}
		getEntityManager().remove(entity);
		return Response.noContent().build();
	}

	// @GET
	// @Produces("application/json")
	// public List<SectionDTO> listAll() {
	// LOG.debug("listAll");
	// final List<Section> searchResults = getEntityManager()
	// .createQuery(
	// "SELECT DISTINCT s FROM Section s LEFT JOIN FETCH s.venue ORDER BY s.id",
	// Section.class).getResultList();
	// final List<SectionDTO> results = new ArrayList<SectionDTO>();
	// for (Section searchResult : searchResults) {
	// SectionDTO dto = new SectionDTO(searchResult);
	// results.add(dto);
	// }
	// return results;
	// }

	@PUT
	@Path("/{id:[0-9][0-9]*}")
	@Consumes("application/json")
	public Response update(@PathParam("id") Long id, SectionDTO dto) {
		LOG.debug("update {}", id);

		TypedQuery<Section> findByIdQuery = getEntityManager()
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
		entity = dto.fromDTO(entity, getEntityManager());
		entity = getEntityManager().merge(entity);
		return Response.noContent().build();
	}
}
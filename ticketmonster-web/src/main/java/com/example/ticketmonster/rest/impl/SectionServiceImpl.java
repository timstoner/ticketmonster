package com.example.ticketmonster.rest.impl;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.core.UriInfo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.example.ticketmonster.model.EntityFactory;
import com.example.ticketmonster.model.Section;
import com.example.ticketmonster.rest.SectionService;
import com.example.ticketmonster.rest.dto.SectionDTO;

public class SectionServiceImpl extends BaseEntityService<Section> implements
		SectionService {

	private static Logger LOG = LoggerFactory
			.getLogger(SectionServiceImpl.class);

	public SectionServiceImpl() {
		super(Section.class);
	}

	public Response create(SectionDTO dto) {
		LOG.debug("create {}", dto.getName());

		// convert dto to entity
		Section entity = EntityFactory.buildSection(dto);

		persist(entity);

		// build a new path to the created section
		String path = String.valueOf(entity.getId());
		URI uri = UriBuilder.fromResource(SectionService.class).path(path)
				.build();

		return Response.created(uri).build();
	}



	@Override
	public Response findById(Long id) {
		Section entity = getSingleInstance(id);

		if (entity != null) {
			SectionDTO dto = new SectionDTO(entity);
			return Response.ok(dto).build();
		} else {
			return Response.status(Status.NOT_FOUND).build();
		}
	}

	@Override
	public Response findAll(UriInfo uriInfo) {
		List<Section> entities = this.getAll(uriInfo.getQueryParameters());
		List<SectionDTO> dtoResults = new ArrayList<>();

		// convert entities to data transfer objects
		for (Section entity : entities) {
			SectionDTO dto = new SectionDTO(entity);
			dtoResults.add(dto);
		}

		return Response.ok(dtoResults).build();
	}

	public Response deleteById(Long id) {
		LOG.debug("deleteById {}", id);
		ResponseBuilder rb;

		// lookup the section by id in the entity manager
		Section entity = getEntityManager().find(Section.class, id);

		if (entity != null) {
			getEntityManager().remove(entity);
			rb = Response.noContent();
		} else {
			rb = Response.status(Status.NOT_FOUND);
		}

		return rb.build();
	}

	public Response update(Long id, SectionDTO dto) {
		LOG.debug("update {}", id);

		Section entity = getSingleInstance(id);

		if (entity != null) {
			entity = dto.fromDTO(entity, getEntityManager());
			entity = getEntityManager().merge(entity);
		}

		return Response.noContent().build();
	}

	protected String getFindAllQuery() {
		return "SELECT DISTINCT s FROM Section s LEFT JOIN FETCH s.venue ORDER BY s.id";
	}

	protected String getFindByIdQuery() {
		return "SELECT DISTINCT s FROM Section s LEFT JOIN FETCH s.venue WHERE s.id = :entityId ORDER BY s.id";
	}

}
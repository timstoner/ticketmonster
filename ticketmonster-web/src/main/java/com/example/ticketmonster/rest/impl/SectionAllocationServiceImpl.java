package com.example.ticketmonster.rest.impl;

import java.net.URI;
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

import com.example.ticketmonster.model.SectionAllocation;
import com.example.ticketmonster.rest.SectionAllocationService;
import com.example.ticketmonster.rest.dto.SectionAllocationDTO;

public class SectionAllocationServiceImpl implements SectionAllocationService {

	private static final Logger LOG = LoggerFactory
			.getLogger(SectionAllocationServiceImpl.class);

	@PersistenceContext
	private EntityManager em;

	public Response create(SectionAllocationDTO dto) {
		LOG.debug("create");

		SectionAllocation entity = dto.fromDTO(null, em);
		em.persist(entity);

		String path = String.valueOf(entity.getId());
		URI uri = UriBuilder.fromResource(SectionAllocationServiceImpl.class)
				.path(path).build();

		return Response.created(uri).build();
	}

	public Response deleteById(Long id) {
		LOG.debug("deleteById {}", id);
		SectionAllocation entity = em.find(SectionAllocation.class, id);

		Response response;

		if (entity != null) {
			em.remove(entity);
			response = Response.noContent().build();
		} else {
			response = Response.status(Status.NOT_FOUND).build();
		}

		return response;
	}

	public Response findById(Long id) {
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

	public Response update(Long id, SectionAllocationDTO dto) {
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
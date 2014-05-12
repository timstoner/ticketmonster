package com.example.ticketmonster.rest.impl;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.NoResultException;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.ws.rs.Path;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.UriBuilder;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.example.ticketmonster.model.Show;
import com.example.ticketmonster.rest.BaseEntityService;
import com.example.ticketmonster.rest.ShowService;
import com.example.ticketmonster.rest.dto.ShowDTO;

@Path("/shows")
public class ShowServiceImpl extends BaseEntityService<Show> implements
		ShowService {

	private static Logger LOG = LoggerFactory.getLogger(ShowServiceImpl.class);

	public ShowServiceImpl() {
		super(Show.class);
	}

	public Response create(ShowDTO dto) {
		LOG.debug("create {}", dto.getDisplayTitle());
		Show entity = dto.fromDTO(null, getEntityManager());
		getEntityManager().persist(entity);

		String path = String.valueOf(entity.getId());
		URI uri = UriBuilder.fromResource(ShowServiceImpl.class).path(path)
				.build();
		LOG.info("Show Created uri: {}", uri);

		return Response.created(uri).build();
	}

	public Response deleteById(Long id) {
		LOG.debug("deleteById {}", id);

		Show entity = getEntityManager().find(Show.class, id);
		ResponseBuilder rb;

		if (entity != null) {
			LOG.info("Delete Show Id: {}", entity.getId());
			getEntityManager().remove(entity);
			rb = Response.noContent();
		} else {
			LOG.warn("Cannot delete show, Show Id Not Found: {}", id);
			rb = Response.status(Status.NOT_FOUND);
		}

		return rb.build();
	}

	public Response update(Long id, ShowDTO dto) {
		LOG.debug("update {}", id);

		TypedQuery<Show> findByIdQuery = getEntityManager()
				.createQuery(
						"SELECT DISTINCT s FROM Show s LEFT JOIN FETCH s.event LEFT JOIN FETCH s.venue LEFT JOIN FETCH s.performances LEFT JOIN FETCH s.ticketPrices WHERE s.id = :entityId ORDER BY s.id",
						Show.class);
		findByIdQuery.setParameter("entityId", id);

		Show entity;
		try {
			entity = findByIdQuery.getSingleResult();
		} catch (NoResultException nre) {
			entity = null;
		}

		entity = dto.fromDTO(entity, getEntityManager());
		entity = getEntityManager().merge(entity);
		return Response.noContent().build();
	}

	@Override
	protected Predicate[] extractPredicates(
			MultivaluedMap<String, String> queryParameters,
			CriteriaBuilder criteriaBuilder, Root<Show> root) {

		List<Predicate> predicates = new ArrayList<Predicate>();

		if (queryParameters.containsKey("venue")) {
			String venue = queryParameters.getFirst("venue");
			predicates.add(criteriaBuilder.equal(root.get("venue").get("id"),
					venue));
		}

		if (queryParameters.containsKey("event")) {
			String event = queryParameters.getFirst("event");
			predicates.add(criteriaBuilder.equal(root.get("event").get("id"),
					event));
		}
		return predicates.toArray(new Predicate[] {});
	}

	public Show getShowByPerformance(Long performanceId) {
		LOG.debug("getShowByPerformance {}", performanceId);

		Query query = getEntityManager()
				.createQuery(
						"select s from Show s where exists(select p from Performance p where p.show = s and p.id = :performanceId)");
		query.setParameter("performanceId", performanceId);
		return (Show) query.getSingleResult();
	}
}

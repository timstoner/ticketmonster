package com.example.ticketmonster.rest.impl;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.core.UriInfo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.example.ticketmonster.model.Event;
import com.example.ticketmonster.rest.EventService;
import com.example.ticketmonster.rest.dto.EventDTO;

/**
 * <p>
 * A JAX-RS endpoint for handling {@link Event}s. Inherits the actual methods
 * from {@link BaseEntityService}, but implements additional search criteria.
 * </p>
 * 
 * @author Marius Bogoevici
 */

public class EventServiceImpl extends BaseEntityService<Event> implements
		EventService {

	private static Logger LOG = LoggerFactory.getLogger(EventServiceImpl.class);

	public EventServiceImpl() {
		super(Event.class);
	}

	public Response create(EventDTO dto) {
		LOG.debug("create {}", dto.getName());

		Event entity = Event.buildEvent(dto, getEntityManager());
		persist(entity);

		// build uri to new entity
		String path = String.valueOf(entity.getId());
		URI uri = UriBuilder.fromResource(EventService.class).path(path)
				.build();

		return Response.created(uri).build();
	}

	public Response update(Long id, EventDTO dto) {
		LOG.debug("update {}", id);
		Event entity = getSingleInstance(id);

		if (entity != null) {
			entity = Event.buildEvent(dto, getEntityManager());
			// entity = dto.fromDTO(entity, getEntityManager());
			getEntityManager().merge(entity);
		}

		return Response.noContent().build();
	}

	@Override
	public Response findById(Long id) {
		Event entity = getSingleInstance(id);

		if (entity != null) {
			EventDTO dto = entity.buildDTO();
			return Response.ok(dto).build();
		} else {
			return Response.status(Status.NOT_FOUND).build();
		}
	}

	@Override
	public Response findAll(UriInfo uriInfo) {
		List<Event> entities = this.getAll(uriInfo.getQueryParameters());
		List<EventDTO> dtoResults = new ArrayList<EventDTO>();

		// convert entities to data transfer objects
		for (Event entity : entities) {
			EventDTO dto = entity.buildDTO();
			dtoResults.add(dto);
		}

		return Response.ok(dtoResults).build();
	}

	@Override
	public Response deleteById(Long id) {
		LOG.debug("deleteById {}", id);
		return super.deleteById(id);
	}

	@Override
	public Response deleteAll(UriInfo uriInfo) {
		LOG.debug("deleteAll");
		return super.deleteAll(uriInfo);
	}

	/**
	 * <p>
	 * We override the method from parent in order to add support for additional
	 * search criteria for events.
	 * </p>
	 * 
	 * @param queryParameters
	 *            - the HTTP query parameters received by the endpoint
	 * @param criteriaBuilder
	 *            - @{link CriteriaBuilder} used by the invoker
	 * @param root
	 * @{link Root} used by the invoker
	 * @return
	 */
	@Override
	protected Predicate[] extractPredicates(
			MultivaluedMap<String, String> queryParameters,
			CriteriaBuilder criteriaBuilder, Root<Event> root) {
		List<Predicate> predicates = new ArrayList<Predicate>();

		if (queryParameters.containsKey("category")) {
			String category = queryParameters.getFirst("category");
			predicates.add(criteriaBuilder.equal(
					root.get("category").get("id"), category));
		}

		return predicates.toArray(new Predicate[] {});
	}

	@Override
	protected String getFindByIdQuery() {
		return "SELECT DISTINCT e FROM Event e LEFT JOIN FETCH e.mediaItem LEFT JOIN FETCH e.category WHERE e.id = :entityId ORDER BY e.id";
	}

	@Override
	protected String getFindAllQuery() {
		return "SELECT DISTINCT e FROM Event e LEFT JOIN FETCH e.mediaItem LEFT JOIN FETCH e.category ORDER BY e.id";
	}
}

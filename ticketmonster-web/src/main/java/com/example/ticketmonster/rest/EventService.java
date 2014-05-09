package com.example.ticketmonster.rest;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.UriBuilder;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.example.ticketmonster.model.Event;
import com.example.ticketmonster.rest.dto.EventDTO;

/**
 * <p>
 * A JAX-RS endpoint for handling {@link Event}s. Inherits the actual methods
 * from {@link BaseEntityService}, but implements additional search criteria.
 * </p>
 * 
 * @author Marius Bogoevici
 */
@Path("/events")
public class EventService extends BaseEntityService<Event, EventDTO> {
	private static Logger LOG = LoggerFactory.getLogger(EventService.class);

	public EventService() {
		super(Event.class, EventDTO.class);
	}

	@POST
	@Consumes("application/json")
	public Response create(EventDTO dto) {
		LOG.debug("create {}", dto.getName());

		Event entity = dto.fromDTO(null, getEntityManager());
		getEntityManager().persist(entity);
		return Response.created(
				UriBuilder.fromResource(EventService.class)
						.path(String.valueOf(entity.getId())).build()).build();
	}

	@DELETE
	@Path("/{id:[0-9][0-9]*}")
	public Response deleteById(@PathParam("id") Long id) {
		Event entity = getEntityManager().find(Event.class, id);
		if (entity == null) {
			return Response.status(Status.NOT_FOUND).build();
		}
		getEntityManager().remove(entity);
		return Response.noContent().build();
	}

	@PUT
	@Path("/{id:[0-9][0-9]*}")
	@Consumes("application/json")
	public Response update(@PathParam("id") Long id, EventDTO dto) {
		TypedQuery<Event> findByIdQuery = getEntityManager()
				.createQuery(
						"SELECT DISTINCT e FROM Event e LEFT JOIN FETCH e.mediaItem LEFT JOIN FETCH e.category WHERE e.id = :entityId ORDER BY e.id",
						Event.class);
		findByIdQuery.setParameter("entityId", id);
		Event entity;
		try {
			entity = findByIdQuery.getSingleResult();
		} catch (NoResultException nre) {
			entity = null;
		}
		entity = dto.fromDTO(entity, getEntityManager());
		entity = getEntityManager().merge(entity);
		return Response.noContent().build();
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
}

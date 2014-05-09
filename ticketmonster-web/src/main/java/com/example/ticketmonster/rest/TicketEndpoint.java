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

import com.example.ticketmonster.model.Ticket;
import com.example.ticketmonster.rest.dto.TicketDTO;

@Path("/tickets")
public class TicketEndpoint {
	@PersistenceContext
	private EntityManager em;

	@POST
	@Consumes("application/json")
	public Response create(TicketDTO dto) {
		Ticket entity = dto.fromDTO(null, em);
		em.persist(entity);
		return Response.created(
				UriBuilder.fromResource(TicketEndpoint.class)
						.path(String.valueOf(entity.getId())).build()).build();
	}

	@DELETE
	@Path("/{id:[0-9][0-9]*}")
	public Response deleteById(@PathParam("id") Long id) {
		Ticket entity = em.find(Ticket.class, id);
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
		TypedQuery<Ticket> findByIdQuery = em
				.createQuery(
						"SELECT DISTINCT t FROM Ticket t LEFT JOIN FETCH t.ticketCategory WHERE t.id = :entityId ORDER BY t.id",
						Ticket.class);
		findByIdQuery.setParameter("entityId", id);
		Ticket entity;
		try {
			entity = findByIdQuery.getSingleResult();
		} catch (NoResultException nre) {
			entity = null;
		}
		if (entity == null) {
			return Response.status(Status.NOT_FOUND).build();
		}
		TicketDTO dto = new TicketDTO(entity);
		return Response.ok(dto).build();
	}

	@GET
	@Produces("application/json")
	public List<TicketDTO> listAll() {
		final List<Ticket> searchResults = em
				.createQuery(
						"SELECT DISTINCT t FROM Ticket t LEFT JOIN FETCH t.ticketCategory ORDER BY t.id",
						Ticket.class).getResultList();
		final List<TicketDTO> results = new ArrayList<TicketDTO>();
		for (Ticket searchResult : searchResults) {
			TicketDTO dto = new TicketDTO(searchResult);
			results.add(dto);
		}
		return results;
	}

	@PUT
	@Path("/{id:[0-9][0-9]*}")
	@Consumes("application/json")
	public Response update(@PathParam("id") Long id, TicketDTO dto) {
		TypedQuery<Ticket> findByIdQuery = em
				.createQuery(
						"SELECT DISTINCT t FROM Ticket t LEFT JOIN FETCH t.ticketCategory WHERE t.id = :entityId ORDER BY t.id",
						Ticket.class);
		findByIdQuery.setParameter("entityId", id);
		Ticket entity;
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
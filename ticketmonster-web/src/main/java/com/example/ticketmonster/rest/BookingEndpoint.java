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

import com.example.ticketmonster.model.Booking;
import com.example.ticketmonster.rest.dto.BookingDTO;

@Path("forge/bookings")
public class BookingEndpoint {

	private static Logger LOG = LoggerFactory.getLogger(BookingEndpoint.class);

	@PersistenceContext
	private EntityManager em;

	@POST
	@Consumes("application/json")
	public Response create(BookingDTO dto) {
		LOG.debug("Create: {}", dto.getId());

		Booking entity = dto.fromDTO(null, em);
		em.persist(entity);
		return Response.created(
				UriBuilder.fromResource(BookingEndpoint.class)
						.path(String.valueOf(entity.getId())).build()).build();
	}

	@DELETE
	@Path("/{id:[0-9][0-9]*}")
	public Response deleteById(@PathParam("id") Long id) {
		LOG.debug("deleteById: {}", id);

		Booking entity = em.find(Booking.class, id);
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
		LOG.debug("findById: {}", id);

		TypedQuery<Booking> findByIdQuery = em
				.createQuery(
						"SELECT DISTINCT b FROM Booking b LEFT JOIN FETCH b.tickets LEFT JOIN FETCH b.performance WHERE b.id = :entityId ORDER BY b.id",
						Booking.class);
		findByIdQuery.setParameter("entityId", id);
		Booking entity;
		try {
			entity = findByIdQuery.getSingleResult();
		} catch (NoResultException nre) {
			entity = null;
		}
		if (entity == null) {
			return Response.status(Status.NOT_FOUND).build();
		}
		BookingDTO dto = new BookingDTO(entity);
		return Response.ok(dto).build();
	}

	@GET
	@Produces("application/json")
	public List<BookingDTO> listAll() {
		LOG.debug("listAll");

		final List<Booking> searchResults = em
				.createQuery(
						"SELECT DISTINCT b FROM Booking b LEFT JOIN FETCH b.tickets LEFT JOIN FETCH b.performance ORDER BY b.id",
						Booking.class).getResultList();
		final List<BookingDTO> results = new ArrayList<BookingDTO>();
		for (Booking searchResult : searchResults) {
			BookingDTO dto = new BookingDTO(searchResult);
			results.add(dto);
		}
		return results;
	}

	@PUT
	@Path("/{id:[0-9][0-9]*}")
	@Consumes("application/json")
	public Response update(@PathParam("id") Long id, BookingDTO dto) {
		LOG.debug("update {} ", id);

		TypedQuery<Booking> findByIdQuery = em
				.createQuery(
						"SELECT DISTINCT b FROM Booking b LEFT JOIN FETCH b.tickets LEFT JOIN FETCH b.performance WHERE b.id = :entityId ORDER BY b.id",
						Booking.class);
		findByIdQuery.setParameter("entityId", id);
		Booking entity;
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
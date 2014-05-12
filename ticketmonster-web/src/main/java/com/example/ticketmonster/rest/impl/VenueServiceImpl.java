package com.example.ticketmonster.rest.impl;

import java.net.URI;

import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.UriBuilder;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.example.ticketmonster.model.Venue;
import com.example.ticketmonster.rest.BaseEntityService;
import com.example.ticketmonster.rest.VenueService;
import com.example.ticketmonster.rest.dto.VenueDTO;

/**
 * <p>
 * A JAX-RS endpoint for handling {@link Venue}s. Inherits the actual methods
 * from {@link BaseEntityService}.
 * </p>
 * 
 * @author Marius Bogoevici
 */
public class VenueServiceImpl extends BaseEntityService<Venue> implements
		VenueService {
	private static final Logger LOG = LoggerFactory
			.getLogger(VenueServiceImpl.class);

	public VenueServiceImpl() {
		super(Venue.class);
	}

	public Response update(Long id, Venue dto) {
		LOG.debug("update {}", id);

		TypedQuery<Venue> findByIdQuery = getEntityManager()
				.createQuery(
						"SELECT DISTINCT v FROM Venue v LEFT JOIN FETCH v.sections LEFT JOIN FETCH v.mediaItem WHERE v.id = :entityId ORDER BY v.id",
						Venue.class);
		findByIdQuery.setParameter("entityId", id);
		Venue entity;
		try {
			entity = findByIdQuery.getSingleResult();
		} catch (NoResultException nre) {
			entity = null;
		}

		entity = getEntityManager().find(Venue.class, id);
		entity = getEntityManager().merge(entity);
		return Response.noContent().build();
	}
}
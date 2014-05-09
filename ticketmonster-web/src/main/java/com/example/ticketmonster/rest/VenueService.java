package com.example.ticketmonster.rest;

import java.net.URI;

import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.UriBuilder;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.example.ticketmonster.model.Venue;
import com.example.ticketmonster.rest.dto.VenueDTO;

/**
 * <p>
 * A JAX-RS endpoint for handling {@link Venue}s. Inherits the actual methods
 * from {@link BaseEntityService}.
 * </p>
 * 
 * @author Marius Bogoevici
 */
@Path("/venues")
public class VenueService extends BaseEntityService<Venue, VenueDTO> {
	private static final Logger LOG = LoggerFactory
			.getLogger(VenueService.class);

	public VenueService() {
		super(Venue.class, VenueDTO.class);
	}

	@POST
	@Consumes("application/json")
	public Response create(VenueDTO dto) {
		LOG.debug("create {}", dto.getName());

		Venue entity = dto.fromDTO(null, getEntityManager());
		getEntityManager().persist(entity);

		String path = String.valueOf(entity.getId());
		LOG.info("Venue Created Name: {}, Id: {}, Path: {}", entity.getName(),
				entity.getId(), path);
		URI uri = UriBuilder.fromResource(VenueService.class).path(path)
				.build();

		return Response.created(uri).build();
	}

	@DELETE
	@Path("/{id:[0-9][0-9]*}")
	public Response deleteById(@PathParam("id") Long id) {
		LOG.debug("deleteById {}", id);
		ResponseBuilder rb;

		Venue entity = getEntityManager().find(Venue.class, id);

		if (entity != null) {
			getEntityManager().remove(entity);
			LOG.info("Venue Deleted Id: {}", id);
			rb = Response.noContent();
		} else {
			rb = Response.status(Status.NOT_FOUND);
			LOG.debug("No Venue Found {}", id);
		}

		return rb.build();
	}

//	@GET
//	@Produces("application/json")
//	public List<VenueDTO> listAll() {
//		LOG.debug("listAll");
//
//		final List<Venue> searchResults = getEntityManager()
//				.createQuery(
//						"SELECT DISTINCT v FROM Venue v LEFT JOIN FETCH v.sections LEFT JOIN FETCH v.mediaItem ORDER BY v.id",
//						Venue.class).getResultList();
//		final List<VenueDTO> results = new ArrayList<VenueDTO>();
//		for (Venue searchResult : searchResults) {
//			VenueDTO dto = new VenueDTO(searchResult);
//			results.add(dto);
//		}
//		return results;
//	}

	@PUT
	@Path("/{id:[0-9][0-9]*}")
	@Consumes("application/json")
	public Response update(@PathParam("id") Long id, VenueDTO dto) {
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
		entity = dto.fromDTO(entity, getEntityManager());
		entity = getEntityManager().merge(entity);
		return Response.noContent().build();
	}
}
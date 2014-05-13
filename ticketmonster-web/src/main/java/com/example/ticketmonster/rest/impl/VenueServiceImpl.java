package com.example.ticketmonster.rest.impl;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.core.UriInfo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.example.ticketmonster.model.Venue;
import com.example.ticketmonster.rest.SectionService;
import com.example.ticketmonster.rest.VenueService;
import com.example.ticketmonster.rest.dto.VenueDTO;

public class VenueServiceImpl extends BaseEntityService<Venue> implements
		VenueService {
	private static final Logger LOG = LoggerFactory
			.getLogger(VenueServiceImpl.class);

	@Autowired
	private SectionService sectionService;

	public VenueServiceImpl() {
		super(Venue.class);
	}

	@Override
	public Response create(VenueDTO dto) {
		// convert dto to entity
		Venue entity = dto.fromDTO(null, getEntityManager());
		// persist in database
		getEntityManager().persist(entity);
		// build uri to new entity
		String path = String.valueOf(entity.getId());
		URI uri = UriBuilder.fromResource(VenueService.class).path(path)
				.build();

		return Response.created(uri).build();
	}

	@Override
	public Response findById(Long id) {
		Venue entity = getSingleInstance(id);

		if (entity != null) {
			VenueDTO dto = new VenueDTO(entity);
			return Response.ok(dto).build();
		} else {
			return Response.status(Status.NOT_FOUND).build();
		}
	}

	@Override
	public Response findAll(UriInfo uriInfo) {
		List<Venue> entities = this.getAll(uriInfo.getQueryParameters());
		List<VenueDTO> dtoResults = new ArrayList<>();

		// convert entities to data transfer objects
		for (Venue entity : entities) {
			VenueDTO dto = new VenueDTO(entity);
			dtoResults.add(dto);
		}

		return Response.ok(dtoResults).build();
	}

	@Override
	public Response update(Long id, VenueDTO dto) {
		LOG.debug("update {}", id);

		Venue entity = getSingleInstance(id);

		entity = getEntityManager().find(Venue.class, id);
		entity = getEntityManager().merge(entity);
		return Response.noContent().build();
	}

	//
	// public List<SectionAllocation> findSectionAllocationBySection(
	// Section section) {
	// CriteriaQuery<SectionAllocation> criteria = getEntityManager()
	// .getCriteriaBuilder().createQuery(SectionAllocation.class);
	// Root<SectionAllocation> from = criteria.from(SectionAllocation.class);
	// CriteriaBuilder builder = getEntityManager().getCriteriaBuilder();
	// Predicate sectionIsSame = builder.equal(from.get("section"), section);
	// return getEntityManager().createQuery(
	// criteria.select(from).where(sectionIsSame)).getResultList();
	// }
	//
	// public List<TicketPrice> findTicketPricesBySection(Long id) {
	//
	// Response r = sectionService.findById(id);
	//
	// CriteriaQuery<TicketPrice> criteria = getEntityManager()
	// .getCriteriaBuilder().createQuery(TicketPrice.class);
	// Root<TicketPrice> from = criteria.from(TicketPrice.class);
	// CriteriaBuilder builder = getEntityManager().getCriteriaBuilder();
	// Predicate sectionIsSame = builder.equal(from.get("section"), section);
	// return getEntityManager().createQuery(
	// criteria.select(from).where(sectionIsSame)).getResultList();
	// }

	@Override
	protected String getFindByIdQuery() {
		return "SELECT DISTINCT v FROM Venue v LEFT JOIN FETCH v.sections LEFT JOIN FETCH v.mediaItem WHERE v.id = :entityId ORDER BY v.id";
	}

	@Override
	protected String getFindAllQuery() {
		return "SELECT DISTINCT v FROM Venue v LEFT JOIN FETCH v.sections LEFT JOIN FETCH v.mediaItem ORDER BY v.id";
	}

}
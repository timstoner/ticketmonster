package com.example.ticketmonster.rest.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.example.ticketmonster.dto.VenueDTO;
import com.example.ticketmonster.factory.VenueFactory;
import com.example.ticketmonster.model.Venue;
import com.example.ticketmonster.rest.SectionService;
import com.example.ticketmonster.rest.VenueService;

public class VenueServiceImpl extends BaseEntityService<Venue, VenueDTO>
		implements VenueService {
	private static final Logger LOG = LoggerFactory
			.getLogger(VenueServiceImpl.class);

	@Autowired
	private SectionService sectionService;

	public VenueServiceImpl() {
		super(Venue.class);
		LOG.debug("Creating Venue Service");
	}

//	public List<SectionAllocation> findSectionAllocationBySection(
//			Section section) {
//		CriteriaQuery<SectionAllocation> criteria = getEntityManager()
//				.getCriteriaBuilder().createQuery(SectionAllocation.class);
//		Root<SectionAllocation> from = criteria.from(SectionAllocation.class);
//		CriteriaBuilder builder = getEntityManager().getCriteriaBuilder();
//		Predicate sectionIsSame = builder.equal(from.get("section"), section);
//		return getEntityManager().createQuery(
//				criteria.select(from).where(sectionIsSame)).getResultList();
//	}

//	public List<TicketPrice> findTicketPricesBySection(Long id) {
//		Response r = sectionService.findById(id);
//		CriteriaQuery<TicketPrice> criteria = getEntityManager()
//				.getCriteriaBuilder().createQuery(TicketPrice.class);
//		Root<TicketPrice> from = criteria.from(TicketPrice.class);
//		CriteriaBuilder builder = getEntityManager().getCriteriaBuilder();
//		Predicate sectionIsSame = builder.equal(from.get("section"), section);
//		return getEntityManager().createQuery(
//				criteria.select(from).where(sectionIsSame)).getResultList();
//	}

	@Override
	protected String getFindByIdQuery() {
		return VenueFactory.getFindByIdQuery();
	}

	@Override
	protected String getFindAllQuery() {
		return VenueFactory.getFindAllQuery();
	}

	@Override
	protected Venue buildEntity(VenueDTO dto) {
		Venue entity = VenueFactory.buildEntity(dto, em);
		return entity;
	}

}
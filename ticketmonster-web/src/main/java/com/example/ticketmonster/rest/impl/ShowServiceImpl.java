package com.example.ticketmonster.rest.impl;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.ws.rs.core.MultivaluedMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.example.ticketmonster.model.Show;
import com.example.ticketmonster.rest.ShowService;
import com.example.ticketmonster.rest.dto.ShowDTO;

public class ShowServiceImpl extends BaseEntityService<Show, ShowDTO> implements
		ShowService {

	private static Logger LOG = LoggerFactory.getLogger(ShowServiceImpl.class);

	public ShowServiceImpl() {
		super(Show.class);
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

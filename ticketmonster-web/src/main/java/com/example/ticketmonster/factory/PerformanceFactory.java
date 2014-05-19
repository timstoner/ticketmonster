package com.example.ticketmonster.factory;

import javax.persistence.EntityManager;

import com.example.ticketmonster.dto.NestedPerformanceDTO;
import com.example.ticketmonster.dto.PerformanceDTO;
import com.example.ticketmonster.model.Performance;
import com.example.ticketmonster.model.Show;

public class PerformanceFactory extends EntityFactory {

	public static Performance buildEntity(PerformanceDTO dto, EntityManager em) {
		Performance entity;

		entity = new Performance();
		entity.setId(dto.getId());
		entity.setDate(dto.getDate());
		Show show = ShowFactory.buildEntity(dto.getShow(), em);
		entity.setShow(show);

		return entity;
	}

	public static Performance buildEntity(NestedPerformanceDTO dto,
			EntityManager em) {
		Performance entity;

		entity = findEntity(dto.getId(), em, Performance.class,
				getFindByIdQuery());

		return entity;
	}

	public static String getFindByIdQuery() {
		return "SELECT DISTINCT p FROM Performance p LEFT JOIN FETCH p.show WHERE p.id = :entityId ORDER BY p.id";
	}

	public static String getFindAllQuery() {
		return "SELECT DISTINCT p FROM Performance p LEFT JOIN FETCH p.show ORDER BY p.id";
	}
}

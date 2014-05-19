package com.example.ticketmonster.factory;

import javax.persistence.EntityManager;

import com.example.ticketmonster.dto.SectionAllocationDTO;
import com.example.ticketmonster.model.Performance;
import com.example.ticketmonster.model.Section;
import com.example.ticketmonster.model.SectionAllocation;

public class SectionAllocationFactory extends EntityFactory {

	public static SectionAllocation buildEntity(SectionAllocationDTO dto,
			EntityManager em) {
		SectionAllocation entity;
		
		if (dto.getId() != null) {
			entity = findEntity(dto.getId(), em, SectionAllocation.class,
					getFindByIdQuery());
		} else {
			Section section = SectionFactory.buildSection(dto.getSection(), em);
			Performance performance = PerformanceFactory.buildEntity(
					dto.getPerformance(), em);

			entity = new SectionAllocation(performance, section);
		}

		return entity;
	}

	public static String getFindByIdQuery() {
		return "SELECT DISTINCT s FROM SectionAllocation s LEFT JOIN FETCH s.performance LEFT JOIN FETCH s.section WHERE s.id = :entityId ORDER BY s.id";
	}

	public static String getFindAllQuery() {
		return "SELECT DISTINCT s FROM SectionAllocation s LEFT JOIN FETCH s.performance LEFT JOIN FETCH s.section ORDER BY s.id";
	}
}

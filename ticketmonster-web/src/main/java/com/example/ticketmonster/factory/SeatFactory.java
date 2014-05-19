package com.example.ticketmonster.factory;

import javax.persistence.EntityManager;

import com.example.ticketmonster.dto.SeatDTO;
import com.example.ticketmonster.model.Seat;
import com.example.ticketmonster.model.Section;

public class SeatFactory extends EntityFactory {

	public static Seat buildEntity(SeatDTO dto, EntityManager em) {
		Section section = SectionFactory.buildSection(dto.getSection(), em);
		Seat entity = new Seat(section, dto.getRowNumber(), dto.getNumber());

		return entity;
	}
}

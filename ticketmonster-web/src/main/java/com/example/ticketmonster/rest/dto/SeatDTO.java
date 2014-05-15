package com.example.ticketmonster.rest.dto;

import java.io.Serializable;

import com.example.ticketmonster.model.Seat;
import com.example.ticketmonster.rest.dto.NestedSectionDTO;

import javax.persistence.EntityManager;

public class SeatDTO extends BaseDTO  implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int number;
	private int rowNumber;
	private NestedSectionDTO section;

	public SeatDTO() {
	}

	public SeatDTO(final Seat entity) {
		if (entity != null) {
			this.number = entity.getNumber();
			this.rowNumber = entity.getRowNumber();
			this.section = new NestedSectionDTO(entity.getSection());
		}
	}

	public Seat fromDTO(Seat entity, EntityManager em) {
		if (entity == null) {
			entity = new Seat();
		}
		return entity;
	}

	public int getNumber() {
		return this.number;
	}

	public void setNumber(final int number) {
		this.number = number;
	}

	public int getRowNumber() {
		return this.rowNumber;
	}

	public void setRowNumber(final int rowNumber) {
		this.rowNumber = rowNumber;
	}

	public NestedSectionDTO getSection() {
		return this.section;
	}

	public void setSection(final NestedSectionDTO section) {
		this.section = section;
	}
}
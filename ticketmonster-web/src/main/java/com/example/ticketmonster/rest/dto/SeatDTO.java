package com.example.ticketmonster.rest.dto;

import java.io.Serializable;

public class SeatDTO extends BaseDTO implements Serializable {

	private static final long serialVersionUID = 1L;
	private int number;
	private int rowNumber;
	private NestedSectionDTO section;

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
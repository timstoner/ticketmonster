package com.example.ticketmonster.rest.dto;

import java.io.Serializable;

public class NestedTicketCategoryDTO extends BaseDTO implements Serializable {

	private static final long serialVersionUID = 1L;
	private Long id;
	private String description;

	public Long getId() {
		return this.id;
	}

	public void setId(final Long id) {
		this.id = id;
	}

	public String getDescription() {
		return this.description;
	}

	public void setDescription(final String description) {
		this.description = description;
	}
}
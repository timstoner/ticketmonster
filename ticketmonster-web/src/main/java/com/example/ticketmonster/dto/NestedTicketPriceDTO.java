package com.example.ticketmonster.dto;

import java.io.Serializable;

public class NestedTicketPriceDTO extends BaseDTO implements Serializable {

	private static final long serialVersionUID = 1L;
	private Long id;
	private float price;
	private String displayTitle;

	public Long getId() {
		return this.id;
	}

	public void setId(final Long id) {
		this.id = id;
	}

	public float getPrice() {
		return this.price;
	}

	public void setPrice(final float price) {
		this.price = price;
	}

	public String getDisplayTitle() {
		return this.displayTitle;
	}

	public void setDisplayTitle(String title) {
		this.displayTitle = title;
	}
}

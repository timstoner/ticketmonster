package com.example.ticketmonster.dto;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class TicketDTO extends BaseDTO implements Serializable {

	private static final long serialVersionUID = 1L;
	private Long id;
	private NestedTicketCategoryDTO ticketCategory;
	private float price;
	private SeatDTO seat;

	public Long getId() {
		return this.id;
	}

	public void setId(final Long id) {
		this.id = id;
	}

	public NestedTicketCategoryDTO getTicketCategory() {
		return this.ticketCategory;
	}

	public void setTicketCategory(NestedTicketCategoryDTO ticketCategory) {
		this.ticketCategory = ticketCategory;
	}

	public float getPrice() {
		return this.price;
	}

	public void setPrice(final float price) {
		this.price = price;
	}

	public SeatDTO getSeat() {
		return this.seat;
	}

	public void setSeat(final SeatDTO seat) {
		this.seat = seat;
	}
}
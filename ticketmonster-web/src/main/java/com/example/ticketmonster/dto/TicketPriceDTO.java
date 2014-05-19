package com.example.ticketmonster.dto;

import java.io.Serializable;

import com.example.ticketmonster.dto.NestedSectionDTO;
import com.example.ticketmonster.dto.NestedShowDTO;
import com.example.ticketmonster.dto.NestedTicketCategoryDTO;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class TicketPriceDTO extends BaseDTO implements Serializable {

	private static final long serialVersionUID = 1L;
	private Long id;
	private NestedTicketCategoryDTO ticketCategory;
	private float price;
	private NestedShowDTO show;
	private NestedSectionDTO section;
	private String displayTitle;

	// public TicketPriceDTO() {
	// }
	//
	// public TicketPriceDTO(final TicketPrice entity) {
	// if (entity != null) {
	// this.id = entity.getId();
	// this.ticketCategory = new NestedTicketCategoryDTO(
	// entity.getTicketCategory());
	// this.price = entity.getPrice();
	// this.show = new NestedShowDTO(entity.getShow());
	// this.section = new NestedSectionDTO(entity.getSection());
	// this.displayTitle = entity.toString();
	// }
	// }
	//
	// public TicketPrice fromDTO(TicketPrice entity, EntityManager em) {
	// if (entity == null) {
	// entity = new TicketPrice();
	// }
	// if (this.ticketCategory != null) {
	// entity.setTicketCategory(this.ticketCategory.fromDTO(
	// entity.getTicketCategory(), em));
	// }
	// entity.setPrice(this.price);
	// if (this.show != null) {
	// entity.setShow(this.show.fromDTO(entity.getShow(), em));
	// }
	// if (this.section != null) {
	// entity.setSection(this.section.fromDTO(entity.getSection(), em));
	// }
	// entity = em.merge(entity);
	// return entity;
	// }

	public Long getId() {
		return this.id;
	}

	public void setId(final Long id) {
		this.id = id;
	}

	public NestedTicketCategoryDTO getTicketCategory() {
		return this.ticketCategory;
	}

	public void setTicketCategory(final NestedTicketCategoryDTO ticketCategory) {
		this.ticketCategory = ticketCategory;
	}

	public float getPrice() {
		return this.price;
	}

	public void setPrice(final float price) {
		this.price = price;
	}

	public NestedShowDTO getShow() {
		return this.show;
	}

	public void setShow(final NestedShowDTO show) {
		this.show = show;
	}

	public NestedSectionDTO getSection() {
		return this.section;
	}

	public void setSection(final NestedSectionDTO section) {
		this.section = section;
	}

	public String getDisplayTitle() {
		return this.displayTitle;
	}
}

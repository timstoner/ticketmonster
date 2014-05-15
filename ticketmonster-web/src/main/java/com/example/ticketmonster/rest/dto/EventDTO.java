package com.example.ticketmonster.rest.dto;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class EventDTO extends BaseDTO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Long id;
	private NestedEventCategoryDTO category;
	private NestedMediaItemDTO mediaItem;
	private String description;
	private String name;

	public Long getId() {
		return this.id;
	}

	public void setId(final Long id) {
		this.id = id;
	}

	public NestedEventCategoryDTO getCategory() {
		return this.category;
	}

	public void setCategory(final NestedEventCategoryDTO category) {
		this.category = category;
	}

	public NestedMediaItemDTO getMediaItem() {
		return this.mediaItem;
	}

	public void setMediaItem(final NestedMediaItemDTO mediaItem) {
		this.mediaItem = mediaItem;
	}

	public String getDescription() {
		return this.description;
	}

	public void setDescription(final String description) {
		this.description = description;
	}

	public String getName() {
		return this.name;
	}

	public void setName(final String name) {
		this.name = name;
	}
}
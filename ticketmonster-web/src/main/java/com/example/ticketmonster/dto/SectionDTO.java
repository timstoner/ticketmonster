package com.example.ticketmonster.dto;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlRootElement;

import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@XmlRootElement
public class SectionDTO extends BaseDTO implements Serializable {

	private static final Logger LOG = LoggerFactory.getLogger(SectionDTO.class);

	private static final long serialVersionUID = 1L;
	private Long id;
	private String description;
	private int rowCapacity;
	private String name;
	private int capacity;
	private int numberOfRows;
	private NestedVenueDTO venue;

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

	public int getRowCapacity() {
		return this.rowCapacity;
	}

	public void setRowCapacity(final int rowCapacity) {
		this.rowCapacity = rowCapacity;
	}

	public String getName() {
		return this.name;
	}

	public void setName(final String name) {
		this.name = name;
	}

	public int getCapacity() {
		return this.capacity;
	}

	public void setCapacity(final int capacity) {
		this.capacity = capacity;
	}

	public int getNumberOfRows() {
		return this.numberOfRows;
	}

	public void setNumberOfRows(final int numberOfRows) {
		this.numberOfRows = numberOfRows;
	}

	public NestedVenueDTO getVenue() {
		return this.venue;
	}

	public void setVenue(final NestedVenueDTO venue) {
		this.venue = venue;
	}

	public JSONObject toJSON() {
		JSONObject object = new JSONObject();

		try {
			object.put("capacity", capacity);
			object.put("name", name);
			object.put("id", id);
			object.put("rowCapacity", rowCapacity);
			object.put("numberOfRows", numberOfRows);
			object.put("description", description);
			object.put("venue", venue.toJSON());
		} catch (JSONException e) {
			LOG.warn("Problem building json object", e);
		}

		return object;
	}
}
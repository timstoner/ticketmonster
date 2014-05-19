package com.example.ticketmonster.dto;

import java.io.Serializable;

import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NestedVenueDTO extends BaseDTO implements Serializable {

	private static final Logger LOG = LoggerFactory
			.getLogger(NestedVenueDTO.class);

	private static final long serialVersionUID = 1L;
	private Long id;
	private AddressDTO address;
	private String description;
	private String name;
	private int capacity;

	public Long getId() {
		return this.id;
	}

	public void setId(final Long id) {
		this.id = id;
	}

	public AddressDTO getAddress() {
		return this.address;
	}

	public void setAddress(final AddressDTO address) {
		this.address = address;
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

	public int getCapacity() {
		return this.capacity;
	}

	public void setCapacity(final int capacity) {
		this.capacity = capacity;
	}

	public JSONObject toJSON() {
		JSONObject object = new JSONObject();

		try {
			object.put("capacity", capacity);
			object.put("name", name);
			object.put("id", id);
			object.put("description", description);
			object.put("address", address.toJSON());
		} catch (JSONException e) {
			LOG.warn("Problem building json object", e);
		}

		return object;
	}
}
package com.example.ticketmonster.dto;

import java.io.Serializable;

import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AddressDTO extends BaseDTO implements Serializable {
	private static final Logger LOG = LoggerFactory.getLogger(AddressDTO.class);

	private static final long serialVersionUID = 1L;
	private String street;
	private String country;
	private String city;

	public String getStreet() {
		return this.street;
	}

	public void setStreet(final String street) {
		this.street = street;
	}

	public String getCountry() {
		return this.country;
	}

	public void setCountry(final String country) {
		this.country = country;
	}

	public String getCity() {
		return this.city;
	}

	public void setCity(final String city) {
		this.city = city;
	}

	public JSONObject toJSON() {
		JSONObject address = new JSONObject();

		try {
			address.put("street", street);
			address.put("city", city);
			address.put("country", country);
		} catch (JSONException e) {
			LOG.warn("Problem building json object", e);
		}

		return address;
	}
}
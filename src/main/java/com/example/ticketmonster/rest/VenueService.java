package com.example.ticketmonster.rest;

import javax.ws.rs.Path;

import com.example.ticketmonster.model.Venue;

/**
 * <p>
 * A JAX-RS endpoint for handling {@link Venue}s. Inherits the actual methods
 * from {@link BaseEntityService}.
 * </p>
 * 
 * @author Marius Bogoevici
 */
@Path("/venues")
public class VenueService extends BaseEntityService<Venue> {

	public VenueService() {
		super(Venue.class);
	}

}
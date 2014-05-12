package com.example.ticketmonster.rest;

import javax.ws.rs.Path;

import com.example.ticketmonster.model.Venue;

@Path("/venues")
public interface VenueService extends BaseService<Venue> {

}

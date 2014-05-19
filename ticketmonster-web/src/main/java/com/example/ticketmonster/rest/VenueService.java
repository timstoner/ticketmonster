package com.example.ticketmonster.rest;

import javax.ws.rs.Path;

import com.example.ticketmonster.dto.VenueDTO;

@Path("/venues")
public interface VenueService extends BaseService<VenueDTO> {

}

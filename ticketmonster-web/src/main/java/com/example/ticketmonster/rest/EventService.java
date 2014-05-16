package com.example.ticketmonster.rest;

import javax.ws.rs.Path;

import com.example.ticketmonster.rest.dto.EventDTO;

@Path("/events")
public interface EventService extends BaseService<EventDTO> {

}

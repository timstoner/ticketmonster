package com.example.ticketmonster.rest;

import javax.ws.rs.Path;

import com.example.ticketmonster.dto.EventDTO;

@Path("/events")
public interface EventService extends BaseService<EventDTO> {

}

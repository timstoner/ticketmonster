package com.example.ticketmonster.rest;

import javax.ws.rs.Path;

import com.example.ticketmonster.rest.dto.TicketDTO;

@Path("/tickets")
public interface TicketService extends BaseService<TicketDTO> {

}
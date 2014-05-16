package com.example.ticketmonster.rest;

import javax.ws.rs.Path;

import com.example.ticketmonster.rest.dto.TicketPriceDTO;

@Path("/ticketprices")
public interface TicketPriceService extends BaseService<TicketPriceDTO> {

}
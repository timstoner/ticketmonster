package com.example.ticketmonster.rest;

import javax.ws.rs.Path;

import com.example.ticketmonster.dto.TicketCategoryDTO;

@Path("/ticketcategorys")
public interface TicketCategoryService extends BaseService<TicketCategoryDTO> {

}

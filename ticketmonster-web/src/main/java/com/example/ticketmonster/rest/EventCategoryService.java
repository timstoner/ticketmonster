package com.example.ticketmonster.rest;

import javax.ws.rs.Path;

import com.example.ticketmonster.rest.dto.EventCategoryDTO;

@Path("/eventcategories")
public interface EventCategoryService extends BaseService<EventCategoryDTO> {

}

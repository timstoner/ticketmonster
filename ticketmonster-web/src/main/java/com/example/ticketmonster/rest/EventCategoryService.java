package com.example.ticketmonster.rest;

import javax.ws.rs.Path;

import com.example.ticketmonster.dto.EventCategoryDTO;

@Path("/eventcategories")
public interface EventCategoryService extends BaseService<EventCategoryDTO> {

}

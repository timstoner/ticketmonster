package com.example.ticketmonster.rest;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.example.ticketmonster.rest.dto.BookingDTO;

public interface BookingService extends BaseService {

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	public Response create(BookingDTO dto);

	@PUT
	@Path("/{id:[0-9][0-9]*}")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response update(@PathParam("id") Long id, BookingDTO dto);
}

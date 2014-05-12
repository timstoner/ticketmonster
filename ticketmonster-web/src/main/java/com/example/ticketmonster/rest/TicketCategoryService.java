package com.example.ticketmonster.rest;

import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.example.ticketmonster.rest.dto.TicketCategoryDTO;

@Path("/ticketcategorys")
public interface TicketCategoryService {

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	public Response create(TicketCategoryDTO dto);

	@DELETE
	@Path("/{id:[0-9][0-9]*}")
	public Response deleteById(@PathParam("id") Long id);

	@GET
	@Path("/{id:[0-9][0-9]*}")
	@Produces("application/json")
	public Response findById(@PathParam("id") Long id);

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public List<TicketCategoryDTO> listAll();

	@PUT
	@Path("/{id:[0-9][0-9]*}")
	@Consumes("application/json")
	public Response update(@PathParam("id") Long id, TicketCategoryDTO dto);
}

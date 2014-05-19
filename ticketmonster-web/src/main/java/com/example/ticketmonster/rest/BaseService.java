package com.example.ticketmonster.rest;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import com.example.ticketmonster.dto.BaseDTO;

public interface BaseService<T extends BaseDTO> {

	@GET
	@Path("/count")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getCount(@Context UriInfo uriInfo);

	@GET
	@Path("/{id:[0-9][0-9]*}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response findById(@PathParam("id") Long id);

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response findAll(@Context UriInfo uriInfo);

	@DELETE
	@Path("/{id:[0-9][0-9]*}")
	public Response deleteById(@PathParam("id") Long id);

	@DELETE
	public Response deleteAll(@Context UriInfo uriInfo);

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	public Response create(T dto);

	@PUT
	@Path("/{id:[0-9][0-9]*}")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response update(@PathParam("id") Long id, T dto);
}

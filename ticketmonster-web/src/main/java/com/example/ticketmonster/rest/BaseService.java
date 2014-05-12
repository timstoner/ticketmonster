package com.example.ticketmonster.rest;

import java.util.List;
import java.util.Map;

import javax.transaction.Transactional;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

public interface BaseService<T> {

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Transactional
	public Response create(T dto);

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public List<T> getAll(@Context UriInfo uriInfo);

	@GET
	@Path("/{id:[0-9][0-9]*}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getSingle(@PathParam("id") Long id);

	@GET
	@Path("/count")
	@Produces(MediaType.APPLICATION_JSON)
	public Map<String, Long> getCount(@Context UriInfo uriInfo);

	@DELETE
	@Transactional
	public Response deleteAll();

	@DELETE
	@Path("/{id:[0-9][0-9]*}")
	@Transactional
	public Response deleteById(@PathParam("id") Long id);
}

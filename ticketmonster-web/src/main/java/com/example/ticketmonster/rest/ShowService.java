package com.example.ticketmonster.rest;

import javax.transaction.Transactional;
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

import com.example.ticketmonster.model.Show;
import com.example.ticketmonster.rest.dto.ShowDTO;

public interface ShowService extends BaseService<Show> {

//	@POST
//	@Consumes(MediaType.APPLICATION_JSON)
//	@Transactional
//	public Response create(ShowDTO dto);
//
//	@DELETE
//	@Path("/{id:[0-9][0-9]*}")
//	@Transactional
//	public Response deleteById(@PathParam("id") Long id);
//
//	@PUT
//	@Path("/{id:[0-9][0-9]*}")
//	@Consumes(MediaType.APPLICATION_JSON)
//	@Transactional
//	public Response update(@PathParam("id") Long id, ShowDTO dto);
//
//	@GET
//	@Path("/performance/{id:[0-9][0-9]*}")
//	@Produces(MediaType.APPLICATION_JSON)
//	public Show getShowByPerformance(@PathParam("id") Long performanceId);

}

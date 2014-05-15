package com.example.ticketmonster.rest;

import javax.transaction.Transactional;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.example.ticketmonster.rest.dto.SectionDTO;

@Path("/sections")
public interface SectionService extends BaseService {

	@POST
	@Produces(MediaType.TEXT_PLAIN)
	@Transactional
	public Response create(SectionDTO dto);

	@PUT
	@Path("/{id:[0-9][0-9]*}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Transactional
	public Response update(@PathParam("id") Long id, SectionDTO dto);

}

package com.example.ticketmonster.rest;

import javax.transaction.Transactional;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.example.ticketmonster.rest.dto.PerformanceDTO;

@Path("/performances")
public interface PerformanceService extends BaseService {
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Transactional
	public Response create(PerformanceDTO dto);

	@PUT
	@Path("/{id:[0-9][0-9]*}")
	@Consumes("application/json")
	public Response update(@PathParam("id") Long id, PerformanceDTO dto);
}

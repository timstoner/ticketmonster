package com.example.ticketmonster.rest;

import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.example.ticketmonster.model.TicketPrice;
import com.example.ticketmonster.rest.dto.VenueDTO;

@Path("/venues")
public interface VenueService extends BaseService {
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	public Response create(VenueDTO dto);

	@PUT
	@Path("/{id:[0-9][0-9]*}")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response update(@PathParam("id") Long id, VenueDTO dto);

//	@GET
//	@Path("/prices/{sectionId}")
//	public List<TicketPrice> findTicketPricesBySection(
//			@PathParam("sectionId") Long id);
}

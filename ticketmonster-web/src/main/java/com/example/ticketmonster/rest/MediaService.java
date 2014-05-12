package com.example.ticketmonster.rest;

import java.io.File;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

public interface MediaService {
	@GET
	@Path("/cache/{cachedFileName:\\S*}")
	@Produces("*/*")
	public File getCachedMediaContent(
			@PathParam("cachedFileName") String cachedFileName);

	@GET
	@Path("/{id:\\d*}")
	@Produces("*/*")
	public File getMediaContent(@PathParam("id") Long id);
}

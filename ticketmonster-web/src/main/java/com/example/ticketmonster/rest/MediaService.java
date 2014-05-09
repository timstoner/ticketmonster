package com.example.ticketmonster.rest;

import java.io.File;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

import org.springframework.beans.factory.annotation.Autowired;

import com.example.ticketmonster.model.MediaItem;
import com.example.ticketmonster.service.MediaManager;

@Path("/media")
public class MediaService {

	@Autowired
	private MediaManager mediaManager;

	@PersistenceContext
	private EntityManager entityManager;

	@GET
	@Path("/cache/{cachedFileName:\\S*}")
	@Produces("*/*")
	public File getCachedMediaContent(
			@PathParam("cachedFileName") String cachedFileName) {
		return mediaManager.getCachedFile(cachedFileName);
	}

	@GET
	@Path("/{id:\\d*}")
	@Produces("*/*")
	public File getMediaContent(@PathParam("id") Long id) {
		return mediaManager.getCachedFile(mediaManager.getPath(
				entityManager.find(MediaItem.class, id)).getUrl());
	}
}

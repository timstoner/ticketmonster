package com.example.ticketmonster.rest.impl;

import java.io.File;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.ws.rs.Path;

import org.springframework.beans.factory.annotation.Autowired;

import com.example.ticketmonster.model.MediaItem;
import com.example.ticketmonster.rest.MediaService;
import com.example.ticketmonster.service.MediaManager;

@Path("/media")
public class MediaServiceImpl implements MediaService {

	@Autowired
	private MediaManager mediaManager;

	@PersistenceContext
	private EntityManager entityManager;

	@Override
	public File getCachedMediaContent(String cachedFileName) {
		return mediaManager.getCachedFile(cachedFileName);
	}

	@Override
	public File getMediaContent(Long id) {
		return mediaManager.getCachedFile(mediaManager.getPath(
				entityManager.find(MediaItem.class, id)).getUrl());
	}
}

package com.example.ticketmonster.rest.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.example.ticketmonster.dto.MediaItemDTO;
import com.example.ticketmonster.factory.MediaItemFactory;
import com.example.ticketmonster.model.MediaItem;
import com.example.ticketmonster.rest.MediaItemService;

public class MediaItemServiceImpl extends
		BaseEntityService<MediaItem, MediaItemDTO> implements MediaItemService {
	private static Logger LOG = LoggerFactory
			.getLogger(MediaItemServiceImpl.class);

	public MediaItemServiceImpl() {
		super(MediaItem.class);
		LOG.debug("Creating Media Item Service");
	}

	@Override
	protected MediaItem buildEntity(MediaItemDTO dto) {
		return MediaItemFactory.buildEntity(dto, getEntityManager());
	}

	@Override
	protected String getFindAllQuery() {
		return MediaItemFactory.getFindAllQuery();
	}

	@Override
	protected String getFindByIdQuery() {
		return MediaItemFactory.getFindByIdQuery();
	}

}
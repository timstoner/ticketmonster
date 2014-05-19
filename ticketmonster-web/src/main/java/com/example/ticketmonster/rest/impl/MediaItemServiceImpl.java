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
		MediaItem entity = MediaItemFactory
				.buildEntity(dto, getEntityManager());
		return entity;
	}

	@Override
	protected String getFindAllQuery() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected String getFindByIdQuery() {
		// TODO Auto-generated method stub
		return null;
	}

}
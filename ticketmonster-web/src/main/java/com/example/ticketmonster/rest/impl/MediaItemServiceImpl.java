package com.example.ticketmonster.rest.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.example.ticketmonster.model.MediaItem;
import com.example.ticketmonster.rest.MediaItemService;
import com.example.ticketmonster.rest.dto.MediaItemDTO;

public class MediaItemServiceImpl extends
		BaseEntityService<MediaItem, MediaItemDTO> implements MediaItemService {
	private static Logger LOG = LoggerFactory
			.getLogger(MediaItemServiceImpl.class);

	public MediaItemServiceImpl() {
		super(MediaItem.class);
	}

}
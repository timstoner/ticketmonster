package com.example.ticketmonster.model.factory;

import javax.persistence.EntityManager;

import com.example.ticketmonster.model.MediaItem;
import com.example.ticketmonster.rest.dto.MediaItemDTO;
import com.example.ticketmonster.rest.dto.NestedMediaItemDTO;

public class MediaItemFactory extends BaseFactory {

	public static MediaItem buildEntity(NestedMediaItemDTO dto, EntityManager em) {
		MediaItem entity = new MediaItem();
		
		if (dto.getId() != null) {
			entity = findEntity(dto.getId(), em, MediaItem.class,
					getFindByIdQuery());
		} else {
			entity.setId(dto.getId());
			entity.setMediaType(dto.getMediaType());
			entity.setUrl(dto.getUrl());
		}

		return entity;
	}

	public static MediaItem buildEntity(MediaItemDTO dto, EntityManager em) {
		MediaItem entity = new MediaItem();

		if (dto.getId() != null) {
			entity = findEntity(dto.getId(), em, MediaItem.class,
					getFindByIdQuery());
		} else {
			entity.setId(dto.getId());
			entity.setMediaType(dto.getMediaType());
			entity.setUrl(dto.getUrl());
		}

		return entity;
	}

	public static String getFindByIdQuery() {
		return "SELECT DISTINCT m FROM MediaItem m WHERE m.id = :entityId ORDER BY m.id";
	}

	public static String getFindAllQuery() {
		return "SELECT DISTINCT m FROM MediaItem m ORDER BY m.id";
	}
}

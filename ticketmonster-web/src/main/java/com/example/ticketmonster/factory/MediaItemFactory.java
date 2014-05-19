package com.example.ticketmonster.factory;

import javax.persistence.EntityManager;

import com.example.ticketmonster.dto.MediaItemDTO;
import com.example.ticketmonster.dto.NestedMediaItemDTO;
import com.example.ticketmonster.model.MediaItem;

public class MediaItemFactory extends EntityFactory {

	public static MediaItem buildEntity(MediaItemDTO dto, EntityManager em) {
		MediaItem entity = new MediaItem();

		entity.setId(dto.getId());
		entity.setMediaType(dto.getMediaType());
		entity.setUrl(dto.getUrl());

		return entity;
	}

	public static MediaItem buildEntity(NestedMediaItemDTO dto, EntityManager em) {
		MediaItem entity = new MediaItem();

		entity = findEntity(dto.getId(), em, MediaItem.class,
				getFindByIdQuery());

		return entity;
	}

	public static String getFindByIdQuery() {
		return "SELECT DISTINCT m FROM MediaItem m WHERE m.id = :entityId ORDER BY m.id";
	}

	public static String getFindAllQuery() {
		return "SELECT DISTINCT m FROM MediaItem m ORDER BY m.id";
	}
}

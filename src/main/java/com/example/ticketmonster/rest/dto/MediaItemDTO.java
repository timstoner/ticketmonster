package com.example.ticketmonster.rest.dto;

import java.io.Serializable;

import javax.persistence.EntityManager;

import com.example.ticketmonster.model.MediaItem;
import com.example.ticketmonster.model.MediaType;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class MediaItemDTO implements Serializable {

	private static final long serialVersionUID = 1L;
	private Long id;
	private String url;
	private MediaType mediaType;

	public MediaItemDTO() {
	}

	public MediaItemDTO(final MediaItem entity) {
		if (entity != null) {
			this.id = entity.getId();
			this.url = entity.getUrl();
			this.mediaType = entity.getMediaType();
		}
	}

	public MediaItem fromDTO(MediaItem entity, EntityManager em) {
		if (entity == null) {
			entity = new MediaItem();
		}
		entity.setUrl(this.url);
		entity.setMediaType(this.mediaType);
		entity = em.merge(entity);
		return entity;
	}

	public Long getId() {
		return this.id;
	}

	public void setId(final Long id) {
		this.id = id;
	}

	public String getUrl() {
		return this.url;
	}

	public void setUrl(final String url) {
		this.url = url;
	}

	public MediaType getMediaType() {
		return this.mediaType;
	}

	public void setMediaType(final MediaType mediaType) {
		this.mediaType = mediaType;
	}
}
package com.example.ticketmonster.rest.dto;

import java.io.Serializable;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.example.ticketmonster.model.MediaItem;
import com.example.ticketmonster.model.MediaType;

public class NestedMediaItemDTO extends BaseDTO implements Serializable {
	private static final Logger LOG = LoggerFactory
			.getLogger(NestedMediaItemDTO.class);
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Long id;
	private String url;
	private MediaType mediaType;

	public NestedMediaItemDTO() {
	}

//	public NestedMediaItemDTO(final MediaItem entity) {
//		if (entity != null) {
//			this.id = entity.getId();
//			this.url = entity.getUrl();
//			this.mediaType = entity.getMediaType();
//		}
//	}

//	public MediaItem fromDTO(MediaItem entity, EntityManager em) {
//		if (entity == null) {
//			entity = new MediaItem();
//		}
//		if (this.id != null) {
//			TypedQuery<MediaItem> findByIdQuery = em
//					.createQuery(
//							"SELECT DISTINCT m FROM MediaItem m WHERE m.id = :entityId",
//							MediaItem.class);
//			findByIdQuery.setParameter("entityId", this.id);
//			try {
//				entity = findByIdQuery.getSingleResult();
//			} catch (javax.persistence.NoResultException nre) {
//				entity = null;
//			}
//			return entity;
//		}
//		entity.setUrl(this.url);
//		entity.setMediaType(this.mediaType);
//		entity = em.merge(entity);
//		return entity;
//	}

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

	public JSONObject toJSON() {
		JSONObject mediaitem = new JSONObject();

		try {
			mediaitem.put("mediaType", mediaType);
			mediaitem.put("url", url);
			mediaitem.put("id", id);
		} catch (JSONException e) {
			LOG.warn("Problem building json object", e);
		}

		return mediaitem;
	}
}
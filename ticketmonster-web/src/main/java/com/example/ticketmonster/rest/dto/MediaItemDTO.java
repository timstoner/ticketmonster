package com.example.ticketmonster.rest.dto;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlRootElement;

import com.example.ticketmonster.model.MediaType;

@XmlRootElement
public class MediaItemDTO extends BaseDTO implements Serializable {

	private static final long serialVersionUID = 1L;
	private Long id;
	private String url;
	private MediaType mediaType;

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
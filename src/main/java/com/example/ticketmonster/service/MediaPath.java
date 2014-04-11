package com.example.ticketmonster.service;

import org.springframework.stereotype.Component;

import com.example.ticketmonster.model.MediaType;

@Component
public class MediaPath {

	private final String url;
	private final boolean cached;
	private final MediaType mediaType;

	public MediaPath(String url, boolean cached, MediaType mediaType) {
		this.url = url;
		this.cached = cached;
		this.mediaType = mediaType;
	}

	public String getUrl() {
		return url;
	}

	public boolean isCached() {
		return cached;
	}

	public MediaType getMediaType() {
		return mediaType;
	}

}

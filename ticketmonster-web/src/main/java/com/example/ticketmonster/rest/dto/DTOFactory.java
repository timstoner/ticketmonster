package com.example.ticketmonster.rest.dto;

import java.io.IOException;

import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DTOFactory {
	private static final Logger LOG = LoggerFactory.getLogger(DTOFactory.class);

	public static <T> T build(Class<T> clas, String entity) {
		ObjectMapper mapper = new ObjectMapper();
		T dto = null;
		try {
			dto = mapper.readValue(entity, clas);
		} catch (IOException e) {
			LOG.warn("Problem building DTO from JSON", e);
		}
		return dto;
	}
}

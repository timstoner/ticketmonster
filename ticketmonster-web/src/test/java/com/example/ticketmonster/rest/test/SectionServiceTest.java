package com.example.ticketmonster.rest.test;

import static org.junit.Assert.assertEquals;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.cxf.jaxrs.client.WebClient;
import org.codehaus.jettison.json.JSONArray;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.example.ticketmonster.dto.SectionDTO;
import com.example.ticketmonster.dto.VenueDTO;
import com.example.ticketmonster.factory.EntityFactory;
import com.example.ticketmonster.model.Section;

public class SectionServiceTest extends BaseServiceTest {

	private static final Logger LOG = LoggerFactory
			.getLogger(SectionServiceTest.class);

	@Test
	public void testGetSectionsWithWebClient() {
		LOG.info("running testGetSectionsWithWebClient");

		WebClient client = WebClient.create(ENDPOINT_ADDRESS);
		client.path("sections");
		Response response = client.get();

		JSONArray array = getJSONArrayFromResponse(response);
		int length = array.length();
		assertEquals(23, length);
	}

	@Test
	public void testGetSectionWithWebClient() {
		LOG.info("running testGetSectionsWithWebClient");
		WebClient client = WebClient.create(ENDPOINT_ADDRESS);

		long sectionId = 1;
		client.path("sections/" + sectionId);

		// send get request to endpoint
		Response response = client.get();

		// convert response to dto
		SectionDTO responseDTO = convertResponse(SectionDTO.class, response);

		Section section = entityManager.find(Section.class, sectionId);
		SectionDTO entityDTO = section.buildDTO();

		assertEquals(entityDTO, responseDTO);
	}

	@Test
	public void testCreateSectionWithWebClient() {
		LOG.info("running testCreateSectionWithWebClient");
		WebClient client = WebClient.create(ENDPOINT_ADDRESS);
		client.path("sections");
		client.type(MediaType.APPLICATION_JSON);

		// create new venue
		VenueDTO venueDTO = generateRandomVenue();
		SectionDTO sectionDTO = buildRandomSection();
		
		LOG.debug("Post: {}", sectionDTO.toJSON());

		Response response = client.post(sectionDTO.toString());
		String path = response.readEntity(String.class);

		LOG.debug(path);
		assertEquals(201, response.getStatus());
	}

}

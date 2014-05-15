package com.example.ticketmonster.rest.test;

import static org.junit.Assert.assertEquals;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.cxf.jaxrs.client.WebClient;
import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONObject;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.example.ticketmonster.model.Section;
import com.example.ticketmonster.rest.dto.DTOFactory;
import com.example.ticketmonster.rest.dto.SectionDTO;
import com.example.ticketmonster.rest.dto.VenueDTO;

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

		// convert json response to a string
		String entity = response.readEntity(String.class);

		// convert json string to a section dto
		SectionDTO responseDTO = DTOFactory.build(SectionDTO.class, entity);

		Section section = entityManager.find(Section.class, sectionId);
		SectionDTO entityDTO = new SectionDTO(section);

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

		Response response = client.post(sectionDTO);
		String path = response.readEntity(String.class);

		LOG.debug(path);
		assertEquals(201, response.getStatus());
	}

}

package com.example.ticketmonster.rest.test;

import static org.junit.Assert.assertEquals;

import java.net.URI;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.cxf.jaxrs.client.WebClient;
import org.codehaus.jettison.json.JSONArray;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.example.ticketmonster.model.Venue;
import com.example.ticketmonster.rest.dto.VenueDTO;

public class VenueServiceTest extends BaseServiceTest {
	private static final Logger LOG = LoggerFactory
			.getLogger(VenueServiceTest.class);

	@Test
	public void testGetVenuesWithWebClient() {
		LOG.info("running testGetEventsWithWebClient");
		WebClient client = WebClient.create(ENDPOINT_ADDRESS);
		client.path("venues");
		// send get request to endpoint
		Response response = client.get();

		// convert response to a json array
		JSONArray array = getJSONArrayFromResponse(response);

		// get number of results returned
		int length = array.length();
		// 5 sample venues imported into database
		assertEquals(5, length);
	}

	@Test
	public void testGetVenueWithWebClient() {
		LOG.info("running testGetEventWithWebClient");

		long id = 1;
		// get venue with id
		VenueDTO responseDTO = getVenue(id);

		// find venue in database
		Venue venue = entityManager.find(Venue.class, id);
		// convert entity to dto
		VenueDTO entityDTO = venue.buildDTO();

		// assert they are equal
		assertEquals(entityDTO, responseDTO);
	}

	@Test
	public void testNoVenueFoundWithWebClient() {
		LOG.info("running testNoVenueFoundWithWebClient");

		WebClient client = WebClient.create(ENDPOINT_ADDRESS);
		client.path("venues/500");
		Response response = client.get();

		assertEquals(404, response.getStatus());
	}

	@Test
	public void testCreateVenueWithWebClient() {
		LOG.info("running testCreateVenueWithWebClient");

		WebClient client = WebClient.create(ENDPOINT_ADDRESS);
		client.path("venues");
		client.type(MediaType.APPLICATION_JSON);
		client.accept(MediaType.APPLICATION_JSON);

		VenueDTO venueDTO = generateRandomVenue();

		Response response = client.post(venueDTO.toJSON());
		LOG.debug("Create Post Request Response Status: {}",
				response.getStatus());

		assertEquals(201, response.getStatus());

		URI newVenueURI = response.getLocation();
		String path = newVenueURI.getPath();
		LOG.debug("id: " + path.charAt(path.length() - 1));
	}

	protected VenueDTO getVenue(long id) {
		WebClient client = WebClient.create(ENDPOINT_ADDRESS);
		client.path("venues/" + id);
		Response response = client.get();
		return convertResponse(VenueDTO.class, response);
	}
}

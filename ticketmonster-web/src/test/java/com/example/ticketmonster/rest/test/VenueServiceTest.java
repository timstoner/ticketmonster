package com.example.ticketmonster.rest.test;

import static org.junit.Assert.assertEquals;

import javax.ws.rs.core.Response;

import org.apache.cxf.jaxrs.client.WebClient;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.example.ticketmonster.rest.dto.VenueDTO;

public class VenueServiceTest extends BaseServiceTest {
	private static final Logger LOG = LoggerFactory
			.getLogger(VenueServiceTest.class);

	@Test
	public void testGetEventsWithWebClient() {
		LOG.info("running testGetEventsWithWebClient");
		WebClient client = WebClient.create(TestUtils.ENDPOINT_ADDRESS);
		client.path("venues");
		Response response = client.get();

		String entity = response.readEntity(String.class);
		LOG.debug(entity);
		// List<Venue> venues = response.readEntity(List.class);
	}

	@Test
	public void testGetEventWithWebClient() {
		LOG.info("running testGetEventWithWebClient");

		WebClient client = WebClient.create(TestUtils.ENDPOINT_ADDRESS);
		client.path("venues/1");
		Response response = client.get();

		String entity = response.readEntity(String.class);
		LOG.debug(entity);
		JSONObject object;

		try {
			object = new JSONObject(entity);
			long id = (int) object.get("id");
//			assertEquals(Long.valueOf(1L), id);
		} catch (JSONException e) {
			LOG.error("Error handling JSON Object", e);
		}
	}
}

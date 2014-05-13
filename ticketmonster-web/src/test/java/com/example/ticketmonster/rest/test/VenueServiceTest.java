package com.example.ticketmonster.rest.test;

import static org.junit.Assert.assertEquals;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.cxf.jaxrs.client.WebClient;
import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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

		JSONArray array;

		try {
			array = new JSONArray(entity);
			int length = array.length();
			// 5 sample venues imported into database
			assertEquals(5, length);
		} catch (JSONException e) {
			LOG.error("Error handling JSON Object", e);
		}
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
			assertEquals(1L, id);
		} catch (JSONException e) {
			LOG.error("Error handling JSON Object", e);
		}
	}

	@Test
	public void testNoVenueFoundWithWebClient() {
		LOG.info("running testNoVenueFoundWithWebClient");

		WebClient client = WebClient.create(TestUtils.ENDPOINT_ADDRESS);
		client.path("venues/500");
		Response response = client.get();

		assertEquals(404, response.getStatus());
	}

	@Test
	public void testCreateVenueWithWebClient() {
		LOG.info("running testCreateVenueWithWebClient");

		WebClient client = WebClient.create(TestUtils.ENDPOINT_ADDRESS);
		client.path("venues");
		client.type(MediaType.APPLICATION_JSON);
		client.accept(MediaType.APPLICATION_JSON);

		JSONObject venue = buildVenue();
		Response response = client.post(venue);
		LOG.debug("Create Post Request Response Status: {}",
				response.getStatus());

		assertEquals(200, response.getStatus());
	}

	protected JSONObject buildVenue() {
		JSONObject venue = new JSONObject();

		int id = random.nextInt(1000);
		JSONObject address = generateAddress();
		JSONObject mediaItem = generateMediaItem();
		String description = randomString(50);

		int totalCapacity = 0;
		JSONArray sections = new JSONArray();
		int sectioncount = random.nextInt(5) + 1;

		int capacity;
		JSONObject section;
		try {
			for (int i = 0; i < sectioncount; i++) {
				section = generateSection();
				capacity = Integer.valueOf(section.get("capacity").toString());
				totalCapacity += capacity;
				sections.put(section);
			}
		} catch (JSONException e) {
			LOG.warn("Problem generating sections", e);
		}

		try {
			venue.put("capacity", totalCapacity);
			venue.put("id", id);
			venue.put("description", description);
			venue.put("address", address);
			venue.put("mediaItem", mediaItem);
		} catch (JSONException e) {
			LOG.warn("Problem building address", e);
		}

		return venue;
	}
}

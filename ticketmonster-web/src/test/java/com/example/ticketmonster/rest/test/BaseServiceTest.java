package com.example.ticketmonster.rest.test;

import java.util.Random;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.ws.rs.core.Response;

import org.apache.cxf.endpoint.Server;
import org.apache.cxf.jaxrs.JAXRSServerFactoryBean;
import org.apache.cxf.jaxrs.client.WebClient;
import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.junit.After;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.example.ticketmonster.dto.AddressDTO;
import com.example.ticketmonster.dto.NestedVenueDTO;
import com.example.ticketmonster.dto.SectionDTO;
import com.example.ticketmonster.dto.VenueDTO;
import com.example.ticketmonster.factory.EntityFactory;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:applicationContext-test.xml" })
public abstract class BaseServiceTest {
	private static final Logger LOG = LoggerFactory
			.getLogger(BaseServiceTest.class);

	protected Server server;
	public final static String ENDPOINT_ADDRESS = "http://localhost:8951/rest";
	public final static String WADL_ADDRESS = ENDPOINT_ADDRESS + "?_wadl";

	private static final String alphabet = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ_!@#$%^&*() {}|";
	protected static Random random = new Random();

	@Autowired
	protected ApplicationContext context;

	@PersistenceContext
	protected EntityManager entityManager;

	@Before
	public void initialize() throws Exception {
		LOG.info("Initializing server");
		JAXRSServerFactoryBean sf = context
				.getBean(JAXRSServerFactoryBean.class);
		sf.setAddress(ENDPOINT_ADDRESS);

		server = sf.create();
	}

	@After
	public void destroy() throws Exception {
		LOG.info("Destroying server");
		server.stop();
		server.destroy();
	}

	public WebClient getWebClient(String path) {
		WebClient client = context.getBean("webClient", WebClient.class);
		client.path(path);
		return client;
	}

	protected VenueDTO generateRandomVenue() {
		VenueDTO dto = new VenueDTO();

		dto.setAddress(generateRandomAddress());
		dto.setCapacity(100);
		dto.setDescription(randomString(100));

		return dto;
	}

	protected AddressDTO generateRandomAddress() {
		AddressDTO dto = new AddressDTO();

		dto.setCity(randomString(10));
		dto.setStreet(randomString(10));
		dto.setCountry(randomString(10));

		return dto;
	}

	protected JSONObject generateMediaItem() {
		JSONObject mediaitem = new JSONObject();

		String mediaType = "IMAGE";
		String url = randomString(15);
		int id = random.nextInt(100);

		try {
			mediaitem.put("mediaType", mediaType);
			mediaitem.put("url", url);
			mediaitem.put("id", id);
		} catch (JSONException e) {
			LOG.warn("Problem generate media item json object", e);
		}

		return mediaitem;
	}

	protected AddressDTO buildAddress(JSONObject address) {
		AddressDTO addressDTO = new AddressDTO();

		try {
			addressDTO.setCity(address.getString("city"));
			addressDTO.setStreet(address.getString("street"));
			addressDTO.setCountry(address.getString("country"));
		} catch (JSONException e) {
			LOG.error("Problem parsing address json object", e);
		}

		return addressDTO;
	}

	protected JSONObject generateSection() {
		String name = randomString();
		String description = randomString(20);
		int numberOfRows = 10;
		int rowCapacity = 10;

		return buildSection(name, description, numberOfRows, rowCapacity);
	}

	protected SectionDTO buildRandomSection() {
		SectionDTO dto = new SectionDTO();
		dto.setNumberOfRows(10);
		dto.setDescription(randomString(200));
		dto.setRowCapacity(10);
		dto.setCapacity(100);
		dto.setVenue(generateRandomNestedVenue());

		return dto;
	}

	protected NestedVenueDTO generateRandomNestedVenue() {
		return null;
	}

	protected JSONObject buildSection(String name, String description,
			int numberOfRows, int rowCapacity) {
		JSONObject section = new JSONObject();
		int capacity = numberOfRows * rowCapacity;

		try {
			section.put("name", name);
			section.put("description", description);
			section.put("numberOfRows", numberOfRows);
			section.put("rowCapacity", rowCapacity);
			section.put("capacity", capacity);
		} catch (JSONException e) {
			LOG.warn("Problem building section", e);
		}

		return section;
	}

	protected String randomString() {
		int length = random.nextInt(20);
		return randomString(length);
	}

	protected String randomString(int len) {
		StringBuilder sb = new StringBuilder(len);
		for (int i = 0; i < len; i++) {
			sb.append(alphabet.charAt(random.nextInt(alphabet.length())));
		}
		return sb.toString();
	}

	protected JSONArray getJSONArrayFromResponse(Response response) {
		JSONArray object = new JSONArray();
		String entity = response.readEntity(String.class);
		LOG.debug(entity);
		try {
			object = new JSONArray(entity);
		} catch (JSONException e) {
			LOG.error("Error handling JSON Array", e);
		}

		return object;
	}

	protected JSONObject getJSONObjectFromResponse(Response response) {
		JSONObject object = new JSONObject();
		String entity = response.readEntity(String.class);
		LOG.debug(entity);
		try {
			object = new JSONObject(entity);
		} catch (JSONException e) {
			LOG.error("Error handling JSON Object", e);
		}

		return object;
	}

	protected long getId(JSONObject object) {
		long id = -1;
		try {
			id = (int) object.get("id");
		} catch (JSONException e) {
			LOG.error("No Id Found in JSON Object", e);
		}
		return id;
	}

	protected static <T> T convertResponse(Class<T> type, Response response) {
		String entity = response.readEntity(String.class);
		T responseDTO = EntityFactory.build(type, entity);
		return responseDTO;
	}
}

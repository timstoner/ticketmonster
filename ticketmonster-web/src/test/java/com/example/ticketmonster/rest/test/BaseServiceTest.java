package com.example.ticketmonster.rest.test;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.apache.cxf.endpoint.Server;
import org.apache.cxf.jaxrs.JAXRSServerFactoryBean;
import org.apache.cxf.jaxrs.client.WebClient;
import org.apache.cxf.jaxrs.provider.json.JSONProvider;
import org.codehaus.jackson.annotate.JsonAutoDetect.Visibility;
import org.codehaus.jackson.annotate.JsonMethod;
import org.codehaus.jackson.jaxrs.JacksonJaxbJsonProvider;
import org.codehaus.jackson.jaxrs.JacksonJsonProvider;
import org.codehaus.jackson.map.ObjectMapper;
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

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:applicationContext-test.xml" })
public abstract class BaseServiceTest {
	private static final Logger LOG = LoggerFactory
			.getLogger(BaseServiceTest.class);

	protected Server server;

	private static final String alphabet = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ_!@#$%^&*() {}|";
	protected static Random random = new Random();

	@Autowired
	protected ApplicationContext context;

	@Before
	public void initialize() throws Exception {
		LOG.info("Initializing server");
		JAXRSServerFactoryBean sf = context
				.getBean(JAXRSServerFactoryBean.class);
		sf.setAddress(TestUtils.ENDPOINT_ADDRESS);
		sf.setProvider(new JacksonJsonProvider());

		server = sf.create();

		TestUtils.waitForWADL();
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

	protected List<Object> getProviders() {
		List<Object> providers = new ArrayList<Object>();
//		ObjectMapper mapper = new ObjectMapper();
//		mapper.setVisibility(JsonMethod.FIELD, Visibility.ANY);
		

		// providers.add(context.getBean(JSONProvider.class));
		JacksonJaxbJsonProvider jackson = new JacksonJaxbJsonProvider();
//		jackson.setMapper(mapper);

		// <property name="dropRootElement" value="true" />C
		// <property name="supportUnwrapped" value="true" />
//		providers.add(mapper);
		providers.add(jackson);

		return providers;
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

	protected JSONObject generateAddress() {
		String name = randomString(10);
		String street = randomString(15);
		String city = randomString(10);
		String country = randomString(10);

		return buildAddress(name, street, city, country);
	}

	protected JSONObject buildAddress(String name, String street, String city,
			String country) {
		JSONObject address = new JSONObject();

		try {
			address.put("name", name);
			address.put("street", street);
			address.put("city", city);
			address.put("country", country);
		} catch (JSONException e) {
			LOG.warn("Problem building address", e);
		}

		return address;
	}

	protected JSONObject generateSection() {
		int id = 0;
		String name = randomString();
		String description = randomString(20);
		int numberOfRows = 10;
		int rowCapacity = 10;

		return buildSection(id, name, description, numberOfRows, rowCapacity);
	}

	protected JSONObject buildSection(int id, String name, String description,
			int numberOfRows, int rowCapacity) {
		JSONObject section = new JSONObject();
		int capacity = numberOfRows * rowCapacity;

		try {
			section.put("id", id);
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
}

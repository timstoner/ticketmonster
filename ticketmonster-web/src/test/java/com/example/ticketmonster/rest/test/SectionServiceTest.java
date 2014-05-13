package com.example.ticketmonster.rest.test;

import javax.ws.rs.core.Response;

import org.apache.cxf.jaxrs.client.WebClient;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SectionServiceTest extends BaseServiceTest {

	private static final Logger LOG = LoggerFactory
			.getLogger(SectionServiceTest.class);

	@Test
	public void testGetSectionsWithWebClient() {
		LOG.info("running testGetSectionsWithWebClient");

		WebClient client = WebClient.create(TestUtils.ENDPOINT_ADDRESS);
		client.path("sections");
		Response response = client.get();

	}

	@Test
	public void testGetSectionWithWebClient() {
		LOG.info("running testGetSectionsWithWebClient");

		WebClient client = WebClient.create(TestUtils.ENDPOINT_ADDRESS);
		client.path("sections/1");
		Response response = client.get();

	}
}

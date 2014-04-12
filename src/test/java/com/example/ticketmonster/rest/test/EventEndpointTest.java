package com.example.ticketmonster.rest.test;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.core.Response;

import org.apache.cxf.endpoint.Server;
import org.apache.cxf.jaxrs.JAXRSServerFactoryBean;
import org.apache.cxf.jaxrs.client.WebClient;
import org.apache.cxf.jaxrs.lifecycle.SingletonResourceProvider;
import org.apache.log4j.Logger;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import com.example.ticketmonster.rest.EventEndpoint;

public class EventEndpointTest {
	private static Logger LOG = Logger.getLogger(EventEndpointTest.class);

	private final static String ENDPOINT_ADDRESS = "http://localhost:8080/rest";
	private final static String WADL_ADDRESS = ENDPOINT_ADDRESS + "?_wadl";
	private static Server server;

	@BeforeClass
	public static void initialize() throws Exception {
		startServer();
		waitForWADL();
	}

	private static void startServer() {
		LOG.info("Starting JAXRS Server");
		JAXRSServerFactoryBean sf = new JAXRSServerFactoryBean();
		sf.setResourceClasses(EventEndpoint.class);

		List<Object> providers = new ArrayList<Object>();
		// add custom providers if any
		sf.setProviders(providers);

		sf.setResourceProvider(EventEndpoint.class,
				new SingletonResourceProvider(new EventEndpoint(), true));
		sf.setAddress(ENDPOINT_ADDRESS);

		server = sf.create();
	}

	private static void waitForWADL() throws Exception {
		WebClient client = WebClient.create(WADL_ADDRESS);
		// wait for 20 secs or so
		for (int i = 0; i < 20; i++) {
			Thread.sleep(1000);
			Response response = client.get();
			LOG.info(response.getStatusInfo().getStatusCode());
			if (response.getStatus() == 200) {
				break;
			}
		}
		// no WADL is available yet - throw an exception or give tests a chance
		// to run anyway
	}

	@AfterClass
	public static void destroy() throws Exception {
		server.stop();
		server.destroy();
	}

	@Test
	public void testGetBookWithWebClient() {
		WebClient client = WebClient.create(ENDPOINT_ADDRESS);
		client.accept("text/xml");
		client.path("forge/events");
		// Book book = client.get(Book.class);
		// assertEquals(123L, book.getId());
	}

}

package com.example.ticketmonster.rest.test;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.core.Response;

import org.apache.cxf.endpoint.Server;
import org.apache.cxf.jaxrs.JAXRSServerFactoryBean;
import org.apache.cxf.jaxrs.client.WebClient;
import org.apache.cxf.jaxrs.lifecycle.SingletonResourceProvider;
import org.apache.cxf.jaxrs.provider.json.JSONProvider;
import org.codehaus.jackson.jaxrs.JacksonJsonProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.example.ticketmonster.rest.EventService;

public class TestUtils {
	private static Logger LOG = LoggerFactory.getLogger(TestUtils.class);
	public final static String ENDPOINT_ADDRESS = "http://localhost:8080/rest";
	public final static String WADL_ADDRESS = ENDPOINT_ADDRESS + "?_wadl";

	public static Server server;

	public static <T> Server startServer(Class<T> class1) {
		LOG.info("Starting JAXRS Server");
		JAXRSServerFactoryBean sf = new JAXRSServerFactoryBean();
		sf.setResourceClasses(EventService.class);
		T t = null;
		try {
			t = class1.newInstance();
		} catch (InstantiationException | IllegalAccessException e) {
			LOG.error("Error instantiating object " + class1.getName(), e);
		}

		List<Object> providers = new ArrayList<Object>();
		JacksonJsonProvider jacksonProvider = new JacksonJsonProvider();
		JSONProvider jsonProvider = new JSONProvider();
		jsonProvider.setDropRootElement(true);
		jsonProvider.setSupportUnwrapped(true);
		providers.add(jacksonProvider);
		providers.add(jsonProvider);

		// add custom providers if any
		sf.setProviders(providers);

		sf.setResourceProvider(class1, new SingletonResourceProvider(t));
		sf.setAddress(ENDPOINT_ADDRESS);

		server = sf.create();
		return server;
	}

	public static void waitForWADL() throws Exception {
		WebClient client = WebClient.create(WADL_ADDRESS);
		// wait for 20 secs or so
		for (int i = 0; i < 20; i++) {
			Thread.sleep(1000);
			Response response = client.get();

			if (response.getStatus() == 200) {
				LOG.info("Server accepting connections");
				break;
			}
		}
		// no WADL is available yet - throw an exception or give tests a chance
		// to run anyway
	}
}

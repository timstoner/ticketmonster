package com.example.ticketmonster.rest.test;

import org.apache.camel.CamelContext;
import org.apache.camel.Endpoint;
import org.apache.camel.EndpointInject;
import org.apache.camel.Produce;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.test.spring.CamelSpringTestSupport;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.test.annotation.DirtiesContext;

public class BotManagerTest extends BaseServiceTest {

	private static final Logger LOG = LoggerFactory
			.getLogger(BotManagerTest.class);

	@Produce(uri = "${botstatusstart.endpoint}")
	protected ProducerTemplate startEndpoint;

	@EndpointInject(uri = "${botstatusend.endpoint}")
	protected MockEndpoint mockEndpoint;

	@Autowired
	private CamelContext camelContext;

	@DirtiesContext
	@Test
	public void testStartStatusMessage() throws Exception {
		LOG.info("running testStartStatusMessage");
		mockEndpoint.expectedBodiesReceived("start");

		// startEndpoint.

		// Endpoint log =
		// camelContext.getEndpoint("log:com.example.ticketmonster.camel.test");

		LOG.debug("sending message");

		startEndpoint.sendBody("start");
		mockEndpoint.assertIsSatisfied();

	}

	// @Override
	// protected AbstractApplicationContext createApplicationContext() {
	// return new ClassPathXmlApplicationContext("applicationContext-test.xml");
	// }

}

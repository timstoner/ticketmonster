package com.example.ticketmonster.rest.test;

import org.apache.cxf.endpoint.Server;
import org.apache.cxf.jaxrs.JAXRSServerFactoryBean;
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
@ContextConfiguration(locations = { "classpath:applicationContext.xml" })
public class BaseServiceTest {
	private static final Logger LOG = LoggerFactory
			.getLogger(BaseServiceTest.class);

	protected Server server;

	@Autowired
	protected ApplicationContext context;

	@Before
	public void initialize() throws Exception {
		LOG.info("Initializing server");
		JAXRSServerFactoryBean sf = context
				.getBean(JAXRSServerFactoryBean.class);
		sf.setAddress(TestUtils.ENDPOINT_ADDRESS);

		server = sf.create();

		TestUtils.waitForWADL();
	}

	@After
	public void destroy() throws Exception {
		LOG.info("Destroying server");
		server.stop();
		server.destroy();
	}
}

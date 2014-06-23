package com.example.ticketmonster.rest.test;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.embedded.JettySolrRunner;
import org.apache.solr.client.solrj.impl.HttpSolrServer;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.client.solrj.response.UpdateResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.apache.solr.common.SolrInputDocument;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SearchServiceTest extends BaseServiceTest {
	private static final Logger LOG = LoggerFactory
			.getLogger(SearchServiceTest.class);

	private SolrServer solrServer;

	private JettySolrRunner solrRunner;

	public static final int SOLR_PORT = 8952;

	@Override
	public void initialize() throws Exception {
		super.initialize();

		LOG.info("Starting Jetty Solr Runner");
		solrRunner = new JettySolrRunner("src/test/resources/solr", "/solr",
				SOLR_PORT);
		solrRunner.start();

		LOG.info("Starting up Http Solr Server");

		solrServer = new HttpSolrServer("http://localhost:" + SOLR_PORT
				+ "/solr/core1");
		LOG.info("Ping: " + solrServer.ping().getStatus());

		for (int i = 0; i < 10; i++) {
			addRandomDocument(i);
		}
	}

	@Override
	public void shutdown() throws Exception {
		super.shutdown();

		LOG.info("Shutting down Http Solr Server");
		solrServer.shutdown();
		LOG.info("Stopping Jetty Solr Runner");
		solrRunner.stop();
	}

	@Test
	public void test() throws IOException {
		LOG.debug("running simulated test");

		SolrQuery query = new SolrQuery("asdf");

		QueryResponse rsp;
		try {
			rsp = solrServer.query(query);
			LOG.debug("Query Response Code: " + rsp.getStatus());

			SolrDocumentList docs = rsp.getResults();
			if (docs != null) {
				for (SolrDocument doc : docs) {
					LOG.debug("Solr Doc: " + doc.toString());
				}
			}
		} catch (SolrServerException e) {
			LOG.warn("Problem talking to solr server", e);
		}
	}

	private void addRandomDocument(int id) {
		SolrInputDocument document = new SolrInputDocument();
		document.addField("id", id);
		document.addField("text_t", "asdf asdf asdf asdf");
		document.addField("test_b", true);
		UpdateResponse resp;
		try {
			// LOG.debug("Adding debug document id=" + id);
			resp = solrServer.add(document);
			// LOG.debug(rep.toString());
			// LOG.debug("Commiting Solr Index");
			resp = solrServer.commit();
			// LOG.debug(rep.toString());
		} catch (SolrServerException | IOException e) {
			LOG.warn("Problem sending document to solr server", e);
		}
	}
}

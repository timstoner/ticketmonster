package com.example.ticketmonster.rest.test;

import java.io.IOException;

import javax.ws.rs.core.Response;

import org.apache.cxf.jaxrs.client.WebClient;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.embedded.JettySolrRunner;
import org.apache.solr.client.solrj.impl.HttpSolrServer;
import org.apache.solr.client.solrj.request.SolrPing;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.client.solrj.response.SolrPingResponse;
import org.apache.solr.client.solrj.response.UpdateResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.apache.solr.common.SolrInputDocument;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SearchServiceTest extends BaseServiceTest {
	private static final Logger LOG = LoggerFactory
			.getLogger(SearchServiceTest.class);

	private SolrServer solrServer;

	private JettySolrRunner solrRunner;

	public static final String SOLR_URL = "http://localhost:8952/solr/core1";

	public static final int SOLR_PORT = 8952;

	@Override
	public void initialize() throws Exception {
		super.initialize();

		startSolrServer();
		pingSolrServer();
		triggerFullIndex();
//		addTestData();

//		System.in.read();
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
	public void testSearchEvent() throws IOException {
		LOG.debug("running testSearchEvent");

		SolrQuery query = new SolrQuery("Rock");

		QueryResponse rsp;
		try {
			rsp = solrServer.query(query);
			LOG.debug("Query Response Code: " + rsp.getStatus());

			SolrDocumentList docs = rsp.getResults();
			if (docs != null) {
				LOG.info("Number of Results Found: " + docs.size());
				for (SolrDocument doc : docs) {
					LOG.info("Solr Doc: " + doc.toString());
				}
			} else {
				LOG.warn("No Results Found");
			}
		} catch (SolrServerException e) {
			LOG.warn("Problem talking to solr server", e);
		}
	}

	private void startSolrServer() throws Exception {
		LOG.info("Starting Jetty Solr Runner");
		solrRunner = new JettySolrRunner("solr", "/solr", SOLR_PORT);
		solrRunner.start();

		LOG.info("Starting up Http Solr Server");
		solrServer = new HttpSolrServer(SOLR_URL);
	}

	private void pingSolrServer() throws SolrServerException, IOException {
		SolrPing ping = new SolrPing();
		SolrPingResponse response = ping.process(solrServer);
		LOG.debug("Solr Ping Response Status: " + response.getStatus());
	}

	private void triggerFullIndex() throws InterruptedException, JSONException,
			SolrServerException, IOException {
		LOG.debug("Triggering Full Index of DIH");
		String diUrl = SOLR_URL + "/dataimport";
		WebClient client = WebClient.create(diUrl
				+ "?command=full-import&wt=json");

		Response resp = client.get();

		JSONObject data = getJSONObjectFromResponse(resp);
		LOG.debug("Full Index Response: " + data);

		boolean busy = true;
		Response response;
		JSONObject js;
		JSONObject message;
		String value;

		int count;

		while (busy) {
			client = WebClient.create(diUrl + "?wt=json");
			response = client.get();
			js = getJSONObjectFromResponse(response);
			// LOG.debug(js.toString());

			message = js.getJSONObject("statusMessages");
			value = message.getString("Total Rows Fetched");
			count = Integer.parseInt(value);
			LOG.info("Indexing... Rows Fetched: " + count);

			value = js.getString("status");
			if (value.equals("busy")) {
				Thread.sleep(500);
			} else if (value.equals("idle")) {
				LOG.info("Finished indexing");
				busy = false;
			} else {
				LOG.debug("unknown status " + value);
			}
		}

		LOG.info("***** Commiting Solr Index");
		UpdateResponse up = solrServer.commit();
		LOG.debug("**** Solr Commit Status: " + up.getStatus());

	}

	private void addTestData() throws SolrServerException, IOException {
		SolrInputDocument doc;
		UpdateResponse up;

		for (int i = 100; i < 115; i++) {
			doc = new SolrInputDocument();
			doc.addField("id", i);
			doc.addField("name_t", "Document #" + i);
			doc.addField("category_t", "Test Document");
			up = solrServer.add(doc);
			LOG.debug("***** Solr Add Doc Status: " + up.getStatus());
		}

		up = solrServer.commit();
		LOG.debug("***** Solr Commit Status: " + up.getStatus());
	}
}

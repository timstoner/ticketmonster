package com.example.ticketmonster.rest.test;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.embedded.EmbeddedSolrServer;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.client.solrj.response.UpdateResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.apache.solr.common.SolrInputDocument;
import org.apache.solr.core.CoreContainer;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SearchServiceTest extends BaseServiceTest {
	private static final Logger LOG = LoggerFactory
			.getLogger(SearchServiceTest.class);

	private SolrServer solrServer;

	@Override
	public void initialize() throws Exception {
		super.initialize();

		LOG.debug("Initializing Embedded Solr Server");

		CoreContainer coreContainer = new CoreContainer("solr");
		solrServer = new EmbeddedSolrServer(coreContainer, "core1");
		for (int i = 0; i < 10; i++) {
			addRandomDocument(i);
		}
	}

	@Test
	public void test() throws IOException {
		LOG.debug("running simulated test");

		SolrQuery query = new SolrQuery("text:asdf");
		QueryResponse rsp;
		try {
			rsp = solrServer.query(query);
			SolrDocumentList docs = rsp.getResults();
			Iterator<SolrDocument> i = docs.iterator();
			while (i.hasNext()) {
				LOG.info(i.next().toString());
			}

		} catch (SolrServerException e) {
			LOG.warn("Problem talking to solr server", e);
		}
	}

	private void addRandomDocument(int id) {
		SolrInputDocument document = new SolrInputDocument();
		document.addField("id", id);
		document.addField("text", "asdf asdf asdf asdf");
		UpdateResponse rep;
		try {
			LOG.debug("Adding debug document id=" + id);
			rep = solrServer.add(document);
			LOG.debug(rep.toString());
			LOG.debug("Commiting Solr Index");
			rep = solrServer.commit();
			LOG.debug(rep.toString());
		} catch (SolrServerException | IOException e) {
			LOG.warn("Problem sending document to solr server", e);
		}
	}
}

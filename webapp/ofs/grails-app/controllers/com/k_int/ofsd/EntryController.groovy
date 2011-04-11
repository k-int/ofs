package com.k_int.ofsd

import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.CommonsHttpSolrServer;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.params.ModifiableSolrParams;
import org.apache.solr.common.*;
import grails.converters.*
import groovy.text.Template
import groovy.text.SimpleTemplateEngine
import groovy.xml.MarkupBuilder

class EntryController {

  def solrServerBean

  def index = { 
    println "Entry: ${params.id}"

    def result = [:]

    ModifiableSolrParams solr_params = new ModifiableSolrParams();
    solr_params.set("q", "aggregator.internal.id:${params.id}")
    QueryResponse response = solrServerBean.query(solr_params);
    SolrDocumentList sdl = response.getResults();
    long record_count = sdl.getNumFound();

    if ( record_count==1 ) {
      def target_solr_doc = sdl.get(0);
      result['entry'] = target_solr_doc
      def dpp_url = target_solr_doc['repo_url_s']
      println "Got repo url: ${dpp_url}"

      switch ( target_solr_doc['restp'] ) {
        case 'ServiceProvider':
          render(view:'ecd',model:result)
          break;
        case 'Service':
          render(view:'fsd',model:result)
          break;
      }
    }

    result
  }
}

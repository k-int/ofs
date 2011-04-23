package com.k_int.ofsd

import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.CommonsHttpSolrServer;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.params.ModifiableSolrParams;
import org.apache.solr.common.*;

import groovyx.net.http.*
import static groovyx.net.http.ContentType.*
import static groovyx.net.http.Method.*

import grails.converters.*
import groovy.text.Template
import groovy.text.SimpleTemplateEngine
import groovy.xml.MarkupBuilder

import org.codehaus.groovy.grails.commons.ApplicationHolder

class EntryController {

  def solrServerBean
  def dppRestBuilder

  def index = { 
    println "Entry: ${params.id}"

    def result = [:]

    ModifiableSolrParams solr_params = new ModifiableSolrParams();
    solr_params.set("q", "aggregator.internal.id:${params.id}")
    solr_params.set("wt","javabin")
    QueryResponse response = solrServerBean.query(solr_params);
    SolrDocumentList sdl = response.getResults();
    long record_count = sdl.getNumFound();

    if ( record_count==1 ) {
      def target_solr_doc = sdl.get(0);
      result['entry'] = target_solr_doc
      def dpp_url = target_solr_doc['repo_url_s']
      println "Got repo url: ${dpp_url}"
      // def source_record_url = "${ApplicationHolder.application.config.ofs.host}${dpp_url}"

      switch ( target_solr_doc['restp'] ) {
        case 'ServiceProvider':
          result['srcdov'] = fetchdoc(ApplicationHolder.application.config.ofs.host,dpp_url);
          render(view:'ecd',model:result)
          break;
        case 'Service':
          render(view:'fsd',model:result)
          result['srcdov'] = fetchdoc(ApplicationHolder.application.config.ofs.host,dpp_url);
          break;
      }
    }

    result
  }

  def fetchdoc(base,targ) {
    println "Fetching ${targ}"
    // def http = new HTTPBuilder( base )
    def result = dppRestBuilder.get( path : targ, query : ['apikey' : ApplicationHolder.application.config.ofs.dpp.apikey] )

    // println "Got ${result}"
    result
  }
}

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

class SiteMapindexController {


  def solrServerBean

  // http://wiki.apache.org/solr/CommonQueryParameters

  def index = {

    println "SiteMapindexController::index"

    def writer = new StringWriter()
    def xml = new MarkupBuilder(writer)

    ModifiableSolrParams solr_params = new ModifiableSolrParams();
    solr_params.set("q", "restp:S*")
    solr_params.set("rows", "0")
    solr_params.set("facet", "true")
    solr_params.set("facet.field","authority_shortcode")
    solr_params.set("facet.limit",300)
    solr_params.set("facet.mincount",1)

    println "Searching..."

    QueryResponse response = solrServerBean.query(solr_params);
    SolrDocumentList sdl = response.getResults();

    //long record_count = sdl.getNumFound();

    println "Producing sitemap... facet fields : ${response.results.facetFields}"

    def auth_codes = response.facetFields[0].values;

    // http://en.wikipedia.org/wiki/Site_map
    xml.sitemapindex(xmlns:'http://www.sitemaps.org/schemas/sitemap/0.9') {
      sitemap() {
        auth_codes.each { auth ->
          url {
            loc("the location url ${auth.name}")
            lastmod("the last modified date")
          }
        }
      }
    }

    render(contentType:'application/xml', text: writer.toString())
  }

}

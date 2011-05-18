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

  static sitemap_data = []
  static long last_generated = 0;

  def solrServerBean

  // http://wiki.apache.org/solr/CommonQueryParameters

  def siteindex = {

    println "SiteMapindexController::index hashCode=${hashCode()}"

    def writer = new StringWriter()
    def xml = new MarkupBuilder(writer)

    ModifiableSolrParams solr_params = new ModifiableSolrParams();
    solr_params.set("q", "*:*")
    solr_params.set("rows", "0")
    solr_params.set("facet", "true")
    solr_params.set("facet.field","authority_shortcode")
    solr_params.set("facet.limit",300)
    solr_params.set("facet.mincount",1)
    solr_params.set("wt","javabin")

    println "Searching..."

    QueryResponse response = solrServerBean.query(solr_params);
    SolrDocumentList sdl = response.getResults();

    //long record_count = sdl.getNumFound();

    println "Producing sitemap... facet fields : ${response.results.facetFields}"

    def auth_codes = response.facetFields[0].values;

    if ( ( sitemap_data.size() == 0 ) ||
         ( System.currentTimeMillis() - last_generated > 86400 ) ) {    // If never generated or > 24 hours

      // Build up a list of the info to go into the site map. 2 stage approach cleaner for xml builder
      auth_codes.each { auth ->
        if ( auth.count > 0 ) {
	  def auth_info = [:]
	  auth_info.name = auth.name
          auth_info.count = auth.count
	  // Work out last modified date
	  ModifiableSolrParams sp = new ModifiableSolrParams();
	  sp.set("q", "authority_shortcode:${auth.name}")
	  sp.set("rows", "1")
	  sp.set("fl", "modified")  
	  sp.set("sort", "timestamp desc")  
	  QueryResponse r2 = solrServerBean.query(sp);
	  SolrDocumentList sdl2 = r2.getResults();
  	  if ( sdl2.size() > 0 ) {
	    auth_info.lastModified = sdl2[0].getFirstValue('modified')
  	  } 
	  sitemap_data.add(auth_info)
        }
      }
      last_generated = System.currentTimeMillis()
    }

    // http://en.wikipedia.org/wiki/Site_map
    // http://en.wikipedia.org/wiki/Sitemap_index
    xml.sitemapindex(xmlns:'http://www.sitemaps.org/schemas/sitemap/0.9') {
      sitemap_data.each { auth ->
        sitemap() {
          loc("${grailsApplication.config.grails.serverURL}/directory/${auth.name}/sitemap")
          lastmod(auth.lastModified)
          mkp.comment("Doc count for this authority: ${auth.count}")
        }
      }
    }

    println "Render...siteindex"

    render(contentType:'application/xml', text: writer.toString())

    println "Complete - siteindex"
  }


  def authsitemap = {

    println "Sitemap for ${params.authority}"

    def writer = new StringWriter()
    def xml = new MarkupBuilder(writer)

    xml.urlset(xmlns:'http://www.example.com/sitemap/0.9') {
      url() {
        log('hello')
        lastmod('hello')
        changefreq('hello')
        priority('hello')
      }
    }

    println "Render - sitemap"

    render(contentType:'application/xml', text: writer.toString())

    println "Complete - siteindex"
  }

}

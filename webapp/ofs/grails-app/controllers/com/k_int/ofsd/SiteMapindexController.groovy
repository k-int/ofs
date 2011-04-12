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

  def index = {

    println "SiteMapindexController::index"

    def writer = new StringWriter()
    def xml = new MarkupBuilder(writer)

    xml.sitemapindex(xmlns:'http://www.sitemaps.org/schemas/sitemap/0.9') {
        sitemap() {
          loc("the location url")
          lastmod("the last modified date")
        }
    }

    render(contentType:'application/xml', text: writer.toString())
  }

}

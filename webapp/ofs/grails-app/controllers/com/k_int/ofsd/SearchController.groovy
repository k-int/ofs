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


class SearchController {

  def facetFieldMapping = [ 'dc.subject.orig_s':'subject', 'authority_shortcode' : 'authority' ]
  def reversemap = ['subject':'dc.subject.orig_s', 'authority':'authority_shortcode', 'flags':'flags']

  def solrServerBean

  def index = { 
  }

  def search = {
    println "Search action Conf=${params.conf}"

    def result = [:]
    def search_results = null;
    def resp = null;
    def records_per_page = params.pagesize ?: 20;

    // def qry = buildLuceneQuery(qry_props);

    def lucene_query = buildQuery(params)

    if ( ( lucene_query != null ) && ( lucene_query.length() > 0 ) )  {
      result['search_results'] = doSearch(lucene_query, 10, null)
      result['qry'] = lucene_query
    }
    else {
      result['noqry'] = true
      render(view:'searchfront',model:result)
    }

    result['facetFieldMappings'] = facetFieldMapping

    result
  }

  def buildQuery(params) {
    boolean conjunction = false;
    StringWriter sw = new StringWriter()

    if ( ( params != null ) && ( params.q != null ) ) {
      sw.write(params.q)

      reversemap.each { mapping ->
        if ( params[mapping.key] != null ) {
          sw.write(" AND ")
    	  sw.write(mapping.value)
          sw.write(":")
          sw.write(params[mapping.key])
        }
      }
    }

    sw.toString()
  }

  def doSearch(qry, records_per_page, defaultSortString) {
    // SolrServer solr = new CommonsHttpSolrServer(solr_base_url);
    ModifiableSolrParams solr_params = new ModifiableSolrParams();

    def lucene_query = qry;


    solr_params.set("q", lucene_query)

    if ( ( params.offset != null ) && ( params.offset.length() > 0 ) )
      solr_params.set("start",params.offset);

    if ( ( params.max != null ) && ( params.max.length() > 0 ) )
      solr_params.set("rows",params.max);

    if ( ( params.sort != null ) && ( params.sort.length() > 0 ) ) {
      solr_params.set("sort",params.sort)
    }
    else if ( (defaultSortString!=null) && ( defaultSortString.length() > 0 ) ) {
      solr_params.set("sort",defaultSortString)
    }

    solr_params.set("facet","true")
    // solr_params.set("facet.field","extra_index_words_s","flags","authority")
    solr_params.set("facet.field","dc.subject.orig_s","flags","authority_shortcode")
    solr_params.set("facet.limit",10)
    solr_params.set("facet.mincount",1)
    solr_params.set("wt","javabin")

    println "solr params : ${solr_params}"


    QueryResponse response = solrServerBean.query(solr_params);
    // println("solr response = " + response);
    SolrDocumentList sdl = response.getResults();
    long record_count = sdl.getNumFound();

    // if ( record_count > 0 ) {
    //  SolrDocument doc = sdl.get(0);
    //}
    //sdl
    response
  }

}

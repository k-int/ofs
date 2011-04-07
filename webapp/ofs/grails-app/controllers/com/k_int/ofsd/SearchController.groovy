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
    }

    result
  }

  def buildQuery(params) {
    params.q
  }

  def doSearch(qry, records_per_page, defaultSortString) {
    // SolrServer solr = new CommonsHttpSolrServer(solr_base_url);
    ModifiableSolrParams solr_params = new ModifiableSolrParams();

    def lucene_query = qry;


    solr_params.set("q", lucene_query)

    if ( ( params.start != null ) && ( params.start.length() > 0 ) )
      solr_params.set("start",params.start);

    if ( ( params.rows != null ) && ( params.rows.length() > 0 ) )
      solr_params.set("rows",params.rows);

    if ( ( params.sort != null ) && ( params.sort.length() > 0 ) ) {
      solr_params.set("sort",params.sort)
    }
    else if ( (defaultSortString!=null) && ( defaultSortString.length() > 0 ) ) {
      solr_params.set("sort",defaultSortString)
    }

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

  def buildLuceneQuery(fields) {

    // println "Build lucene query from : ${fields}"

    boolean conjunction = false;

    StringWriter sw = new StringWriter()
    fields.each { field ->
      if ( conjunction ) {
        sw.write(" AND ")
      }
      else {
        conjunction = true
      }

      if ( ( field.mapping.mappedTo != null ) && ( field.mapping.mappedTo.length() > 0 ) ) {
        sw.write(field.mapping.mappedTo)
        sw.write(":")
        sw.write("\"${field.value}\"")
      }
      else {
        sw.write("${field.value}")
      }

    }
    sw.toString()
  }

}

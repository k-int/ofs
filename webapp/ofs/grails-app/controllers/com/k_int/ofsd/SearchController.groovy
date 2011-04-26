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
  def reversemap = ['subject':'dc.subject.orig_s', 'authority':'authority_shortcode', 'flags':'flags', 'restp':'restp']

  def solrServerBean
  def solrGazBean

  def index = { 
  }

  def search = {

    def starttime = System.currentTimeMillis();

    println "Search action Conf=${params.conf}"

    def result = [:]
    def search_results = null;
    def resp = null;
    def records_per_page = params.pagesize ?: 20;


    if ( ( params.placename != null ) && ( params.placename.length() > 0 ) ) {
      def lucene_query = buildQuery(params)

      def sort_string = null;
      if ( lucene_query.contains("!spatial") ) {
        println "Query contains a spatial component, default sort by distance"
        sort_string = "distance asc";
      }

      if ( ( lucene_query != null ) && ( lucene_query.length() > 0 ) )  {
        result['search_results'] = doSearch(lucene_query, 10, sort_string)
        result['qry'] = lucene_query
      }
    }
    else {
      result['noqry'] = true
      session.lastqry = null;
      render(view:'searchfront',model:result)
    }
   

    result['facetFieldMappings'] = facetFieldMapping

    result['elapsed'] = "" + ( ( System.currentTimeMillis() - starttime ) / 1000 )

    result
  }

  def buildQuery(params) {
    boolean conjunction = false;
    StringWriter sw = new StringWriter()

    // Add in any spatial restriction
    def gaz_response = resolvePlaceName(params.placename)
    if ( gaz_response != null ) {
      println "Result of gaz lookup : ${gaz_response}"
      if ( gaz_response.size() > 0 ) {
        sw.write("{!spatial lat=${gaz_response[0].lat} long=${gaz_response[0].lon} radius=5 unit=miles} ")
      }
    }

    if ( ( params != null ) && ( params.q != null ) ) {
      // If there was a query, process it
      sw.write(params.q)
    }
    else {
      // Search for everything and let the user restrict using filters
      sw.write("*:*")
    }

    reversemap.each { mapping ->
      if ( params[mapping.key] != null ) {
        if ( params[mapping.key].class.isArray() ) {
          params[mapping.key].each { p ->
            sw.write(" AND ")
            sw.write(mapping.value)
            sw.write(":")
            sw.write(p)
          }
        }
        else {
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
    solr_params.set("facet.field","restp", "dc.subject.orig_s","flags","authority_shortcode" )
    solr_params.set("facet.limit",20)
    solr_params.set("facet.mincount",1)
    // solr_params.set("wt","javabin")

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

  def resolvePlaceName(query_input) {

    def gazresp = []

    // Step 1 : See if the input place name matches a fully qualified place name
    println "exact match q params: ${query_input}"

    ModifiableSolrParams solr_params = new ModifiableSolrParams();
    solr_params.set("q", "fqn:\"${query_input}\"")
    solr_params.set("start", 0);
    solr_params.set("rows", "10");

    def response = solrGazBean.query(solr_params);

    // Try and do an exact place name match first of all
    if ( response.getResults().getNumFound() == 1 ) {
      println "Exact place name match..."
      doc = response.getResults().get(0);
      def sr = ['lat':doc['centroid_lat'],'lon':doc['centroid_lon'], 'name':doc['place_name'], 'fqn':doc['fqn'], 'type':doc['type']]
      gazresp.add(sr)
    }
    else {
      // Doing text match on place name...
      solr_params.set("q", "place_name:(${query_input}) OR alias:(${query_input})");
      solr_params.set("sort", "type desc, score desc");

      println "Attempting generic place name match ${solr_params}"
      response = solrGazBean.query(solr_params);
      response.getResults().each { doc ->
        def sr = ['lat':doc['centroid_lat'],'lon':doc['centroid_lon'], 'name':doc['place_name'], 'fqn':doc['fqn'], 'type':doc['type']]
        gazresp.add(sr)
      }
    }

    gazresp
  }

}

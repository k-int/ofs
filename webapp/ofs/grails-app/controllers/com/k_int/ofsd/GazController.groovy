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
import org.codehaus.groovy.grails.commons.ApplicationHolder

class GazController {

    def solrGazBean

    def index = { 
      println "Gaz called"
      def gazresp = doDismaxGazQuery(params.q)
      def results = ["results":gazresp]

      if ( params.callback != null ) {
        render "${params.callback}(${results as JSON})"
      } else {
        render results as JSON
      }
    }

    def doDismaxGazQuery(q) {

    def gazresp = []

    def the_query = q.replaceAll('"',"").toLowerCase()
    
    // Step 1 : See if the input place name matches a fully qualified place name
    println "perform doDismaxGazQuery : \"qzyf ${the_query}\"" // qzyf is our special private "Left anchor"

    ModifiableSolrParams solr_params = new ModifiableSolrParams();
    solr_params.set("q", "fqnidx:\"qzyf ${the_query}\"")
    // solr_params.set("qt", "dismax");
    solr_params.set("sort", "type desc, score desc");
    solr_params.set("fl", "authority,fqn,type,score");
    // solr_params.set("qf", "fqnidx")
    // solr_params.set("pf", "fqnidx")
    //solr_params.set("hl", "true");
    //solr_params.set("hl.fl", "fqnidx")
    //solr_params.set("f.fqnidx.mergeContiguous", "true")
    solr_params.set("start", 0);
    solr_params.set("rows", "15");
    // solr_params.set("fq", "type:\"1. postcode\" OR type:\"3. Locality\" OR type:\"3.locality\" OR type:\"4.PostTown\"");

    def response = solrGazBean.query(solr_params);

    println "Located ${response.getResults().getNumFound()} matching gazetteer records for ${q}...."


    // Try and do an exact place name match first of all
    if ( response.getResults().getNumFound() > 0 ) {
      println "Located some matching gazetteer records...."
      response.getResults().each { doc ->
        def sr = ['fqn':doc['fqn'], 'type':doc['type'], 'id':doc['id']]
        println "adding response : ${sr}"
        gazresp.add(sr)
      }
    }

    gazresp
  }
}

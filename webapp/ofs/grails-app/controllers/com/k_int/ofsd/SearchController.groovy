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

class SearchController {

  def facetFieldMapping = [ 'dc.subject.orig_s':'subject', 'authority_shortcode' : 'authority' ]
  def reversemap = ['subject':'dc.subject.orig_s', 'authority':'authority_shortcode', 'flags':'flags', 'restp':'restp']

  def solrServerBean
  def solrGazBean
  def providerInformationService

  def index = { 
  }

  def search = {

    def starttime = System.currentTimeMillis();

    println "Search action Conf=${params.conf} pis=${providerInformationService}"

    def result = [:]
    result['provserv'] = providerInformationService

    def search_results = null;
    def resp = null;
    def records_per_page = params.pagesize ?: 20;

    if ( ( params.q == null ) && ( params.placename == null ) && ( params.keyword == null ) ) {
      result['noqry'] = true
      session.lastqry = null;
      render(view:'searchfront',model:result)
    }

    def lucene_query = buildQuery(params, result)

    def sort_string = null;
    if ( lucene_query.contains("!spatial") ) {
      println "Query contains a spatial component, default sort by distance"
      sort_string = "distance asc";
    }

    if ( ( lucene_query != null ) && ( lucene_query.length() > 0 ) )  {
      result['search_results'] = doSearch(lucene_query, 10, sort_string, params)
      result['qry'] = lucene_query
    }
    
    result['facetFieldMappings'] = facetFieldMapping
    result['elapsed'] = "" + ( ( System.currentTimeMillis() - starttime ) / 1000 )

    switch(params.format) {
      case 'rss':
      case 'RSS':
        // println "RSS"
        renderRSSResponse( result )
        break;
      case 'atom':
      case 'atom':
        // println "ATOM"
        renderATOMResponse( result )
        break;
      case 'html':
      case 'HTML':
      default:
        // Default will render the search response page....
        break;
    }

    result
  }

  def buildQuery(params, result) {

    boolean conjunction = false;
    boolean explicit_spatial = false;
    StringWriter sw = new StringWriter()

    // Add in any spatial restriction
    if ( ( params.placename != null ) && ( params.placename.length() > 0 ) ) {
      def gaz_response = resolvePlaceName(params.placename)

      if ( gaz_response != null ) {
        println "Result of gaz lookup : ${gaz_response}"
        if ( gaz_response.size() > 0 ) {
          sw.write("{!spatial lat=${gaz_response[0].lat} long=${gaz_response[0].lon} radius=5 unit=miles} ")
          explicit_spatial = true
          result.place = gaz_response[0];
        }
        else {
          println "Unhandled case - placename resolved ${gaz_response.size()} places"
        }
      }
    }

    // q as a paramter is a request for intelligent place/keyword processing
    if ( ( params != null ) && ( params.q != null ) ) {
      // If there was a query, process it
      if ( explicit_spatial ) {
        // We were passed an explicit placename query, just treat q as keywords
        sw.write(params.q)
        result.keywords = qry_analysis_result.newqry;
      }
      else {
        // See if we can separate out place keywords from subject keywords and "Do the right thing" - "TM"
        // def qry_analysis_result = doDismaxGazQuery(params.q,"fqnidx","fqnidx")
        def qry_analysis_result = doDismaxGazQuery(params.q,"place_name alias","place_name alias")
        println "Result of query analysis: ${qry_analysis_result}"
        if ( ( qry_analysis_result.places != null ) && ( qry_analysis_result.places.size() > 0 ) ) {
          sw.write("{!spatial lat=${qry_analysis_result.places[0].lat} long=${qry_analysis_result.places[0].lon} radius=5 unit=miles} ")
          result.place = qry_analysis_result.places[0];
          if ( ( qry_analysis_result.newqry != null ) && ( qry_analysis_result.newqry.length() > 0 ) ) {
            // println "${params.q} is a place query - with terms ${qry_analysis_result.newqry}"
            sw.write(qry_analysis_result.newqry)
            result.keywords = qry_analysis_result.newqry;
          }
          else {
            // println "${params.q} is a place only query - Add a search for everything and just filter"
            sw.write("*:*")
          }
        }
        else {
          if ( ( params.q != null ) && ( params.q.length() > 0 ) ) {
            sw.write(params.q)
            result.keywords = params.q
          }
          else {
            sw.write("*:*")
            result.keywords = params.keyword
          }
        }
 
      }
    }
    else if ( params.keywords != null )  {
      if ( ( params.keywords != null ) && ( params.keywords.length() > 0 ) ) {
        result.keywords=params.keywords
        sw.write(params.keywords)
      }
      else {
        result.keywords="Everything..."
        sw.write("*:*")
      }
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

  def doSearch(qry, records_per_page, defaultSortString, params) {
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
      // Default sort is distance, but if we are processing an RSS search, make the default modified desc instead.
      if ( ( params.format != null ) && ( params.format.equalsIgnoreCase("rss") || params.format.equalsIgnoreCase("atom") ) ) {
        solr_params.set("sort","modified desc")
      }
      else {
        solr_params.set("sort",defaultSortString)
      }
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

    println "Resolve place name in ${query_input}"
    def gazresp = [:]
    gazresp.places = []
    gazresp.newq = "";

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
      def doc = response.getResults().get(0);
      def sr = ['lat':doc['centroid_lat'],'lon':doc['centroid_lon'], 'name':doc['place_name'], 'fqn':doc['fqn'], 'type':doc['type'], 'alias':doc['alaias']]
      gazresp.places.add(sr)
    }
    else {
      // Doing text match on place name...
      solr_params.set("q", "place_name:(${query_input}) OR alias:(${query_input})");
      solr_params.set("sort", "type desc, score desc");

      println "Attempting generic place name match ${solr_params}"
      response = solrGazBean.query(solr_params);
      response.getResults().each { doc ->
        def sr = ['lat':doc['centroid_lat'],'lon':doc['centroid_lon'], 'name':doc['place_name'], 'fqn':doc['fqn'], 'type':doc['type'], 'alias':doc['alaias']]
        gazresp.places.add(sr)
      }
    }

    if ( gazresp.places.length > 0 ) {
      // Remove any instances of postcode or alias from the query
      gazresp.newq = "${query_input}"
      gazresp.newq = query_input.replaceAll("${gazresp.places[0].name}","")
      gazresp.newq = gazresp.newq.replaceAll("${gazresp.places[0].alias}","")
    }

    gazresp
  }

  def doDismaxGazQuery(q,qf,pf) {

    def gazresp = [:]
    gazresp.places = []
    gazresp.newq = "";

    // Step 1 : See if the input place name matches a fully qualified place name
    println "perform doDismaxGazQuery : ${q}."

    // http://localhost:8080/index/gaz/select?q=(Childcare%20Sheffield%20S3%208PZ)&qt=dismax&hl=true&sort=score%20desc&fl=authority,fqn,id,place_name,type,score,alias,text&qf=text&pf=fqnidx&hl.fl=fqnidx&start=0&rows=1
    ModifiableSolrParams solr_params = new ModifiableSolrParams();
    solr_params.set("q", "(${q})")
    solr_params.set("qt", "dismax");
    // solr_params.set("sort", "type desc, score desc");
    solr_params.set("sort", "score desc");
    solr_params.set("fl", "authority,fqn,id,place_name,type,score,centroid_lat,centroid_lon");
    solr_params.set("qf", qf)
    solr_params.set("pf", pf)
    solr_params.set("hl", "true");
    solr_params.set("hl.fl", "fqnidx")
    solr_params.set("f.fqnidx.mergeContiguous", "true")
    solr_params.set("start", 0);
    solr_params.set("rows", "5");
    solr_params.set("fq", "type:\"1. postcode\" OR type:\"3. Locality\" OR type:\"3.locality\" OR type:\"4. PostTown\"");


    println "Trying to resolve place - ${solr_params}"
    def response = solrGazBean.query(solr_params);

    // Try and do an exact place name match first of all
    if ( response.getResults().getNumFound() > 0 ) {
      println "Located some matching gazetteer records...."

      def newq = q.toLowerCase()


      // response = solrGazBean.query(solr_params);
      response.getResults().each { doc ->
        def sr = ['lat':doc['centroid_lat'],'lon':doc['centroid_lon'], 'name':doc['place_name'], 'fqn':doc['fqn'], 'type':doc['type'], 'id':doc['id']]
        println "adding response : ${sr}"
        gazresp.places.add(sr)
      }

      def highlight_string = response.highlighting[gazresp.places[0].id].fqnidx
      highlight_string.each { snippet ->
        def cont_snipped = snippet.replaceAll("<\\/em><em>","")

        println "Looking for matches in ${cont_snipped}"

        cont_snipped.eachMatch('<em>.*?</em>') {  match ->
          def term_to_remove = match.substring(4,match.length()-5).toLowerCase()
          println "Matched: ${term_to_remove} trying to remove that from ${newq}"
          newq = newq.replaceAll(term_to_remove,"")
          println "After replace, newq=${newq}"
        }
      }

      // Now set up the new query string which has all the placename components matched removed
      gazresp.newqry = newq.trim()
    }

    println "At end, new query is ${gazresp.newqry}, located place is ${gazresp.places[0]}"

    gazresp
  }

  def renderRSSResponse(results) {

    def output_elements = buildOutputElements(results.search_results)

    def writer = new StringWriter()
    def xml = new MarkupBuilder(writer)

    xml.rss(version: '2.0') {
      channel {
        title("Open Family Services RSS Response")
        description("Open Family Services RSS Description")
        copyright("(c) Open Family Services")
        "opensearch:totalResults"(results.search_results.results.numFound)
        "opensearch:startIndex"(results.search_results.results.start)
        "opensearch:itemsPerPage"(10)
        output_elements.each { i ->  // For each record
          entry {
            i.each { tuple ->   // For each tuple in the record
              "${tuple[0]}"("${tuple[1]}")
            }
          }
        }
      }
    }

    render(contentType:"application/rss+xml", text: writer.toString())
  }

  def renderATOMResponse(results) {

    def writer = new StringWriter()
    def xml = new MarkupBuilder(writer)
    def output_elements = buildOutputElements(results.search_results)

    xml.feed(xmlns:'http://www.w3.org/2005/Atom') {
        // add the top level information about this feed.
        title("Open Family Services ATOM Response")
        description("Open Family Services ATOM Response")
        copyright("(c) Open Family Services")
        "opensearch:totalResults"(results.search_results.results.numFound)
        "opensearch:startIndex"(results.search_results.results.start)
        "opensearch:itemsPerPage"("10")
        // subtitle("Serving up my content")
        //id("uri:uuid:xxx-xxx-xxx-xxx")
        link(href:"http://www.openfamilyservices.org.uk")
        author {
          name("OFS - OpenFamilyServices")
        }
        //updated sdf.format(new Date());

        // for each entry we need to create an entry element
        output_elements.each { i ->
          entry {
            i.each { tuple ->
                "${tuple[0]}"("${tuple[1]}")
            }
          }
        }
    }

    render(contentType:'application/xtom+xml', text: writer.toString())
  }


  def buildOutputElements(searchresults) {
    // Result is an array of result elements
    def result = []
    searchresults.results.each { doc ->
      def docinfo = [];

      addField("dc.title", "dc.title", doc, docinfo)
      addField("dc.description", "dc.description", doc, docinfo)
      addField("dc.description", "dc.description", doc, docinfo)
      addField("dc.identifier", "guid", doc, docinfo)
      addField("modified", "pubdate", doc, docinfo)
      docinfo.add(["link","${ApplicationHolder.application.config.ofs.pub.baseurl}/ofs/directory/${doc['authority_shortcode']}/${doc['aggregator.internal.id']}"])
      if ( ( doc['lat'] != null ) && ( doc['lng'] != null ) ) {
        docinfo.add(["georss:point","${doc['lat']} ${doc['lng']}"])
      }
      result.add(docinfo)
    }
    // println "Result ${result}"
    result
  }

  def addField(solr_field, output_field, doc, docinfo) {
    doc.getFieldValues(solr_field).each { value ->
      docinfo.add([output_field, value])
    }
  }

  def oldSearch = {

    def starttime = System.currentTimeMillis();

    println "Search action Conf=${params.conf}"

    def result = [:]
    def search_results = null;
    def resp = null;
    def records_per_page = params.pagesize ?: 20;


    if ( ( ( params.placename != null ) && ( params.placename.length() > 0 ) ) ||
         ( ( params.subject != null ) && ( params.subject.length() > 0 ) ) ) {
      def lucene_query = buildQuery(params)

      def sort_string = null;
      if ( lucene_query.contains("!spatial") ) {
        println "Query contains a spatial component, default sort by distance"
        sort_string = "distance asc";
      }

      if ( ( lucene_query != null ) && ( lucene_query.length() > 0 ) )  {
        result['search_results'] = doSearch(lucene_query, 10, sort_string, params)
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

    switch(params.format) {
      case 'rss':
      case 'RSS':
        // println "RSS"
        renderRSSResponse( result )
        break;
      case 'atom':
      case 'atom':
        // println "ATOM"
        renderATOMResponse( result )
        break;
      case 'html':
      case 'HTML':
      default:
        // Default will render the search response page....
        break;
    }

    result
  }

}

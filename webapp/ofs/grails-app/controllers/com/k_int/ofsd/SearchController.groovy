package com.k_int.ofsd

class SearchController {

  def index = { 
  }

  def doSearch(solr_base_url, qry, records_per_page,defaultSortString) {
    SolrServer solr = new CommonsHttpSolrServer(solr_base_url);
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


    QueryResponse response = solr.query(solr_params);
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

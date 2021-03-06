import org.codehaus.groovy.grails.commons.ApplicationHolder

// Place your Spring DSL code here
beans = {
  def grailsApplication = ApplicationHolder.application

  solrServerBean(org.apache.solr.client.solrj.impl.CommonsHttpSolrServer,application.config.ofs.solr.url) { // "http://localhost:8080/index/aggr"
  }

  solrGazBean(org.apache.solr.client.solrj.impl.CommonsHttpSolrServer,application.config.ofs.gaz.url) { // "http://localhost:8080/index/gaz"
  }

  dppRestBuilder(groovyx.net.http.HTTPBuilder, application.config.ofs.host ) {
  }

}

#!/usr/bin/groovy

@Grapes([
  @Grab(group='com.gmongo', module='gmongo', version='0.9.2'),
  @Grab(group='org.apache.httpcomponents', module='httpmime', version='4.1.2'),
  @Grab(group='org.apache.httpcomponents', module='httpclient', version='4.0'),
  @Grab(group='org.codehaus.groovy.modules.http-builder', module='http-builder', version='0.5.0'),
  @Grab(group='org.apache.httpcomponents', module='httpmime', version='4.1.2')
])

import groovy.util.slurpersupport.GPathResult
import static groovyx.net.http.ContentType.*
import static groovyx.net.http.Method.*
import groovyx.net.http.*
import org.apache.http.entity.mime.*
import org.apache.http.entity.mime.content.*
import java.nio.charset.Charset
import org.apache.http.*
import org.apache.http.protocol.*

def target_service = new RESTClient('http://aggregator.openfamilyservices.org.uk/')

// http://aggregator.openfamilyservices.org.uk/index/aggr/select?q=ofsted_urn_s:300808&fl=dc.title,ofsted_urn_s,dc.identifier&wt=json
def result = true;
println("Checking if lready present");
try {
  target_service.request(GET, ContentType.JSON) { request ->
    uri.path='index/aggr/select'
    uri.query = [
      q:"ofsted_urn_s:300808",
      fl:'dc.title,ofsted_urn_s,dc.identifier,aggr.internal.id',
      wt:'json'
    ]
    response.success = { resp, data ->
      println("Data: ${data}");
      if ( data.response.numFound == 0 ) {
        result = false
      }
      else {
        println("Record already present (${data.response.numFound} times) Not uploading.");
      }
    }
    response.failure = { resp ->
      println("Error - ${resp.status}");
      System.out << resp
    }
  }
}
catch ( Exception e ) {
  e.printStackTrace();
}

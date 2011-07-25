package com.k_int.ofsd

import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.CommonsHttpSolrServer;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.params.ModifiableSolrParams;
import org.apache.solr.common.*;

import groovyx.net.http.*
import static groovyx.net.http.ContentType.*
import static groovyx.net.http.Method.*

import grails.converters.*
import groovy.text.Template
import groovy.text.SimpleTemplateEngine
import groovy.xml.MarkupBuilder

import org.codehaus.groovy.grails.commons.ApplicationHolder

import com.k_int.iep.datamodel.*
import java.security.MessageDigest;


class EntryController {

  public static org.apache.commons.collections.LRUMap recent_feedback_map = new org.apache.commons.collections.LRUMap(100)

  def solrServerBean
  def dppRestBuilder
  def providerInformationService
  def jcaptchaService

  def index = { 
    println "Entry action id:${params.id}"

    def result = [:]
    result['provserv'] = providerInformationService

    ModifiableSolrParams solr_params = new ModifiableSolrParams();
    solr_params.set("q", "aggregator.internal.id:${params.id}")
    solr_params.set("wt","javabin")
    QueryResponse sol_response = solrServerBean.query(solr_params);
    SolrDocumentList sdl = sol_response.getResults();
    long record_count = sdl.getNumFound();

    println "Entry page, referrer is ${request.getHeader('referer')}"

    if ( record_count==1 ) {
      def target_solr_doc = sdl.get(0);
      result['entry'] = target_solr_doc
      def dpp_url = target_solr_doc['repo_url_s']
      println "Got repo url: ${dpp_url}, doctp is ${target_solr_doc['restp']}"
   
      // def source_record_url = "${ApplicationHolder.application.config.ofs.host}${dpp_url}"

      switch ( target_solr_doc['restp'] ) {
        case 'ServiceProvider':
          result['srcdoc'] = fetchdoc(ApplicationHolder.application.config.ofs.host,dpp_url);
          render(view:'ecd',model:result)
          break;
        case 'Service':
          result['srcdoc'] = fetchdoc(ApplicationHolder.application.config.ofs.host,dpp_url);
          render(view:'fsd',model:result)
          break;
      }
    }
    else {
      // response.status = 404 //Not Found
      // response.setStatus(404)
      // render(status:404)
      def remote_addr = request.getHeader("X-Forwarded-For") ?: request.getRemoteAddr()
      println "Request for nonexistent resource ${params.id} from ${remote_addr}"
      // response.sendError(404, "${params.id} not found.")
      response.sendError(410, "${params.id} not found.")
      render "${params.id} not found."
    }

    println "Resource processing complete"

    result
  }

  def fetchdoc(base,targ) {
    println "Fetching ${targ}"
    // def http = new HTTPBuilder( base )
    def result = dppRestBuilder.get( path : targ, query : ['apikey' : ApplicationHolder.application.config.ofs.dpp.apikey] )

    // println "Got ${result}"
    result
  }

  def feedback = {
    println "feedback action for ${params.id}"

    def result = [:]

    if ( params.id != null ) {
      ModifiableSolrParams solr_params = new ModifiableSolrParams();
      solr_params.set("q", "aggregator.internal.id:${params.id}")
      solr_params.set("wt","javabin")
      QueryResponse response = solrServerBean.query(solr_params);
      SolrDocumentList sdl = response.getResults();
      long record_count = sdl.getNumFound();
      def remote_addr = request.getHeader("X-Forwarded-For") ?: request.getRemoteAddr()
      result['remote_addr'] = remote_addr

      println "[feedback] Entry page, referrer is ${request.getHeader('referer')}"

      if ( record_count==1 ) {
        def target_solr_doc = sdl.get(0);
        result['entry'] = target_solr_doc
        def dpp_url = target_solr_doc['repo_url_s']
        println "Got repo url: ${dpp_url}"
        result['validation_stamp'] = getValidationHash(params.authority,
                                                       params.id,
                                                       target_solr_doc['dc.title']);
      }

      if ( request.method.equalsIgnoreCase("POST") ) {
        println "Process as POST"
        try {
          if ( ( params.fbemail != null ) && ( params.fbemail.trim().length() > 0 ) &&
               ( params.fbname != null ) && ( params.fbname.trim().length() > 0 ) &&
               ( params.fbtext != null ) && ( params.fbtext.trim().length() > 0 ) ) {
            if ( jcaptchaService.validateResponse("image", session.id, params.fbcaptchaResponse) ) {
              println "Captcha OK"
              processFeedbackForm(params,request,remote_addr);
              render(view:'thanks', model:result)
            }
            else {
              println "Captcha Fail"
              render(view:'feedback', model:result)
            }        
          }
          else {
            println "Some fields null"
            render(view:'feedback', model:result)
          }
        }
        catch ( com.octo.captcha.service.CaptchaServiceException cse ) {
          render(view:'feedback', model:result)
        }
      }
      else {
        println "Process as non-post"
      }
    }
    else {
      println "error"
    }

    result
  }

  def processFeedbackForm(params,request,remote_addr) {
 
    def result = "OK"

    // Step -1 - Verify that the remote address is not in the LRU Cache or it it as, that the submission 
    Long l = recent_feedback_map.get(remote_addr)

    if ( ( l == null ) || ( l.longValue() - System.currentTimeMillis() > 180 ) ) {
   
      // step 0 : recreate the hash and check it matches
      def generated_hash = getValidationHash(params.auth,params.recid,params.recname)

      if ( generated_hash == params.validation_stamp ) {
        println "Hash match.... process"

        // Step 1 : lookup or create the authority record
        println "Finding provider record for ${params.auth}"
        def auth = IEPProvider.findByShortCode(params.auth)
        if ( auth != null ) {
          // Lookup or create the record pertaining to this resource
          println "Got authority.. now process...."
          def resource = IEPResource.findByOwnerAndResourceIdentifier(auth,params.recid)
          if ( resource == null ) {
            println "No existing resource record found for ${params.auth}:${params.recid}.. create one..."
            resource = new IEPResource(owner: auth, resourceIdentifier: params.recid).save(flush:true)
          }

          def res_msg = new IEPResourceMessage(owner:resource,
                                               messageTimeStamp: new java.sql.Timestamp(System.currentTimeMillis()),
                                               contactEmail:params.fbemail,
                                               contactName:params.fbname,
                                               remoteAddr:remote_addr,
                                               message:params.fbtext,
                                               category:params.fbtype).save()

          // Add an entry to the LRU list for this remote IP, and don't accept another form submission for 2 mins.
          recent_feedback_map.put(remote_addr,new Long(System.currentTimeMillis()))
        }
        else {
          println "unknown auth"
        }
      }
      else {
        println "Validation stamp did not match.. someone is trying to do something funky"
        result = "captcha"
      }
    }
    else {
      println "IP Address ${remote_addr} Has submitted feedback in the last 3 minutes. Too fast"
      result = "toomany"
    }

  }

  def getValidationHash(authority_shortcode, record_id, record_title) {
    def hash_str = "${authority_shortcode}, ${record_id}, ${record_title}"
    println "getValidationHash for ${hash_str}"
    def hash_bytes = hash_str.getBytes()
    MessageDigest m = MessageDigest.getInstance("MD5");
    m.update(hash_bytes, 0, hash_bytes.length);
    new BigInteger(1, m.digest()).toString(16);
  }
}

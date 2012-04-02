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

def mongo = new com.gmongo.GMongo()
def db = mongo.getDB("ofs_source_reconcilliation")

println 'Grab page...'
go(db, '887');

mongo.close();

def go(db, authcode) {
  def max_batch_size = 1000;
  def maxts = db.config.findOne(propname='maxts')

  if ( maxts == null ) {
    maxts = [ propname:'maxts', value:0 ]
    db.config.save(maxts);
  }
  else {
    // In testing, reprocess evey time
    maxts.value = 0;
    db.config.save(maxts);
  }

  def ofs_pass = ""
  System.in.withReader {
    print 'ofs pass:'
    ofs_pass = it.readLine()
  }
  
  def dpp = new RESTClient('http://aggregator.openfamilyservices.org.uk/')

  // Add preemtive auth
  dpp.client.addRequestInterceptor( new HttpRequestInterceptor() {
    void process(HttpRequest httpRequest, HttpContext httpContext) {
      String auth = "ofs:${ofs_pass}"
      String enc_auth = auth.bytes.encodeBase64().toString()
      httpRequest.addHeader('Authorization', 'Basic ' + enc_auth);
    }
  })

  // dpp.auth.basic 'ofs', 'ofs_upload_6652'

  def ctr = 0;
  db.ofsted.find( [ lastModified : [ $gt : maxts.value ], authority:authcode ] ).sort(lastModified:1).limit(max_batch_size).each { rec ->
    maxts.value = rec.lastModified
    def ecdrec = genecd(rec);
    post(ecdrec,dpp,rec);
    println("processed[${ctr++}], ${authcode} records, maxts.value updated to ${rec.lastModified}");
  }

  db.config.save(maxts);
}

def genecd(rec) {
  println("posting record ${rec}");
  def writer = new StringWriter()
  def xml = new groovy.xml.MarkupBuilder(writer)
  xml.setOmitEmptyAttributes(true);
  xml.setOmitNullAttributes(true);

  xml.'ProviderDescription'(
                      'xsi:schemaLocation':'http://dcsf.gov.uk/XMLSchema/Childcare http://www.ispp-aggregator.org.uk/f1les/ISPP/docs/schemas/v5/ProviderTypes-v1-1e.xsd',
                      'xmlns' : 'http://dcsf.gov.uk/XMLSchema/Childcare',
                      'xmlns:lg' : 'http://www.esd.org.uk/standards',
                      'xmlns:gms': 'http://www.govtalk.gov.uk/CM/gms',
                      'xmlns:n2' : 'http://www.govtalk.gov.uk/CM/gms-xs',
                      'xmlns:core'  :'http://www.govtalk.gov.uk/core',
                      'xmlns:n3' : 'http://www.govtalk.gov.uk/metadata/egms',
                      'xmlns:con' : 'http://www.govtalk.gov.uk/people/ContactTypes',
                      'xmlns:apd' : 'http://www.govtalk.gov.uk/people/AddressAndPersonalDetails',
                      'xmlns:bs7666' : 'http://www.govtalk.gov.uk/people/bs7666',
                      'xmlns:xsi' : 'http://www.w3.org/2001/XMLSchema-instance') {
    'DC.Title'(rec.name)
    'DC.Identifier'(rec.uri)
    'Description' {
      'DC.DESCRIPTION'(format:'plain',rec.name)
    }
    'DCTerms'('Childcare')
    // 'DC.Subject'('Childcare')
    'DC.Creator'('OFSTED')
    'DC.Publisher'('href':rec.uri,'OFSTED')
    // 'DC.Date.Created'('title')
    // 'DC.Date.Modified'('title')
    'ProviderDetails' {
      'ProviderName'(rec.name)
      'ConsentVisibleAddress'(true)
      'SettingDetails' {
        'TelephoneNumber' {
          if ( rec.contact.size() > 0 ) {
            if ( ( rec.contact[0].startsWith('Telephone number: ') ) && ( rec.contact[0].length() > 18 ) ) {
              'apd:TelNationalNumber'(rec.contact[0].substring(18))
            }
            else {
              'apd:TelNationalNumber'(rec.contact[0])
            }
          }
        }
        'PostalAddress' {
          'apd:A_5LineAddress' {
            rec.address.each { addrline ->
              'apd:Line'(addrline)
            }
            'apd:PostCode'(rec.postcode)
          }
        }
      }
      'ChildcareType'('Childcare on non-domestic premises') // Creche,...
      'ProvisionType'() // CCD/CCN,
      'ChildcareAges'() // CCN,
      'Country'('United Kingdom') // United Kingdom
      'ModificationDate'('')
      'RegistrationDetails'(RegistrationId:rec.ofstedId) {
        'RegistrationDate'(rec.regdate)
        'RegistrationConditions'()
        'RegistrationTypes' {
          'RegistrationType'() // VCR
        }
        'RegistrationStatus' {
          'RegistrationStatus'('ACTV') // ACTV
          'RegistrationStatusStartDate'(rec.regdate) // YYYY-MM-DD
        }
        if ( rec.reports.size() > 0 ) {
          'LastInspection' {
            'InspectionType'()
            'InspectionDate'()  // yyyy-mm-dd
            'InspectionOverallJudgementDescription'()
          }
        }
        'WelfareNoticeHistoryList'()
      }
      'LinkedRegistration'{
      }
      'QualityAssurance' {
        'QualityLevel' {
          'QualityStatus'(Id:1,ItemName:'Unknown',ListName:'QualityAssurance-1.0','Unknown')
        }
      }
      'FromOfsted'('true')
      'OfstedURN'(rec.ofstedId)
    }
  }

  def result = writer.toString();

  new File('./rec.xml') << result

  result;
}


def post(rec, target_service, orig_rec) {

  byte[] rec_as_bytes = rec.getBytes('UTF-8')
  println("Attempting post... [${rec_as_bytes.length}]");
  target_service.request(POST) { request ->
    requestContentType = 'multipart/form-data'
    uri.path='dpp/provider/upload'
    def multipart_entity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE);
    multipart_entity.addPart("owner", new StringBody( 'ofsted', 'text/plain', Charset.forName('UTF-8')))
    def uploaded_file_body_part = new org.apache.http.entity.mime.content.ByteArrayBody(rec_as_bytes, 'text/xml', 'filename.xml')
    multipart_entity.addPart("upload", uploaded_file_body_part);

    request.entity = multipart_entity

    response.success = { resp, data ->
      println("OK");
    }

    response.failure = { resp ->
      println("Error - ${resp.status}");
      System.out << resp
      println("Done\n\n");
    }
  }
    
}

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

def codes_to_process = [ 301, 302, 370, 800, 822, 303, 330, 889, 890, 350, 837, 867, 380, 304, 846, 801, 305, 825, 351, 381, 873, 202, 823, 895, 896, 201, 908, 331, 306, 909, 841, 831, 830, 878, 371, 835, 332, 840, 307, 811, 845, 308, 881, 390, 916, 203, 204, 876, 205, 850, 309, 310, 805, 311, 884, 919, 312, 313, 921, 420, 206, 207, 886, 810, 314, 382, 340, 208, 888, 383, 856, 855, 209, 925, 341, 821, 352, 887, 315, 806, 826, 391, 316, 926, 812, 813, 802, 392, 815, 928, 929, 892, 891, 353, 931, 874, 879, 836, 851, 870, 317, 807, 318, 354, 372, 857, 355, 333, 343, 373, 893, 871, 334, 933, 803, 393, 852, 882, 210, 342, 860, 356, 808, 861, 935, 394, 936, 319, 866, 357, 894, 883, 880, 211, 358, 384, 335, 320, 212, 877, 937, 869, 938, 213, 359, 868, 344, 872, 336, 885, 816 ];

def ofs_pass = ""
System.in.withReader {
  print 'ofs pass:'
  ofs_pass = it.readLine()
}
  
codes_to_process.each { code ->
  println("Process ${code}");
  go(db,ofs_pass,"${code}");
}

// println 'Grab page...'
// go(db, '887');

mongo.close();

def go(db, ofs_pass, authcode) {
  def max_batch_size = 10000;
  
  def maxts = db.config.findOne(propname="${authcode}-maxts".toString())

  println("Inside go for ${authcode}");

  if ( maxts == null ) {
    println("Create new tracking config for this authority");
    maxts = [ propname:"${authcode}-maxts".toString(), value:0 ]
    db.config.save(maxts);
  }
  else {
    // In testing, reprocess evey time
    maxts.value = 0;
    db.config.save(maxts);
  }

  def dpp = new RESTClient('http://aggregator.openfamilyservices.org.uk/')

  // Add preemtive auth
  dpp.client.addRequestInterceptor( new HttpRequestInterceptor() {
    void process(HttpRequest httpRequest, HttpContext httpContext) {
      String auth = "admin:${ofs_pass}"
      String enc_auth = auth.bytes.encodeBase64().toString()
      httpRequest.addHeader('Authorization', 'Basic ' + enc_auth);
    }
  })

  // dpp.auth.basic 'ofs', 'ofs_upload_6652'

  def ctr = 0;
  db.ofsted.find( [ lastModified : [ $gt : maxts.value ], authority:authcode ] ).sort(lastModified:1).limit(max_batch_size).each { rec ->
    maxts.value = rec.lastModified
    def ecdrec = genecd(rec);
    if ( !alreadyPresent(rec.ofstedId,dpp)) {
      post(ecdrec,dpp,rec,authcode);
    }
    println("processed[${ctr++}], ${authcode} records, maxts.value updated to ${rec.lastModified}");
  }

  println("Updating maxts ${maxts}");
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
  result;
}


def post(rec, target_service, orig_rec, authority) {

  byte[] rec_as_bytes = rec.getBytes('UTF-8')
  println("Attempting post... [${rec_as_bytes.length}]");
  try {
    target_service.request(POST) { request ->
      requestContentType = 'multipart/form-data'
      uri.path='dpp/provider/upload'
      def multipart_entity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE);
      multipart_entity.addPart("owner", new StringBody( 'ofsted', 'text/plain', Charset.forName('UTF-8')))
      def uploaded_file_body_part = new org.apache.http.entity.mime.content.ByteArrayBody(rec_as_bytes, 'text/xml', 'filename.xml')
      multipart_entity.addPart("upload", uploaded_file_body_part);

      request.entity = multipart_entity

      response.success = { resp, data ->
        println("OK - Record uploaded. Authority:${authority}, URI:${orig_rec.uri}")
      }

      response.failure = { resp ->
        println("Error - ${resp.status}. Authority:${authority}, URI:${orig_rec.uri}");
        System.out << resp
        println("Done\n\n");
      }
    }
  }
  catch ( Exception e ) {
    e.printStackTrace();
  }
}

def alreadyPresent(ofstedcode, target_service) {
  // http://aggregator.openfamilyservices.org.uk/index/aggr/select?q=ofsted_urn_s:300808&fl=dc.title,ofsted_urn_s,dc.identifier&wt=json
  def result = true;
  println("Checking if ${ofstedcode} already present");
  try {
    target_service.request(GET, ContentType.JSON) { request ->
      uri.path='index/aggr/select'
      uri.query = [
        q:"ofsted_urn_s:${ofstedcode}",
        fl:'dc.title,ofsted_urn_s,dc.identifier,aggr.internal.id',
        wt:'json'
      ]
      response.success = { resp, data ->
        if ( data.response.numFound == 0 ) {
          println("Record ${ofstedcode} not present in service.. uploading!");
          result = false
        }
        else {
          println("Record ${ofstedcode} already present (${data.response.numFound} times) Not uploading.");
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
  result
}

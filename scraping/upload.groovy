#!/usr/bin/groovy


@Grapes([
  @Grab(group='com.gmongo', module='gmongo', version='0.9.2'),
  @Grab(group='org.apache.httpcomponents', module='httpmime', version='4.1.2'),
  @Grab(group='org.apache.httpcomponents', module='httpclient', version='4.0'),
  @Grab(group='org.codehaus.groovy.modules.http-builder', module='http-builder', version='0.5.0')
])

def mongo = new com.gmongo.GMongo()
def db = mongo.getDB("ofs_source_reconcilliation")

println 'Grab page...'
go(db, '887');

mongo.close();

def go(db, authcode) {
  def max_batch_size = 100;
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
  
  def ctr = 0;
  db.ofsted.find( [ lastModified : [ $gt : maxts.value ], authority:authcode ] ).sort(lastModified:1).limit(max_batch_size).each { rec ->
    maxts.value = rec.lastModified
    post(rec);
    println("processed[${ctr++}], ${authcode} records, maxts.value updated to ${rec.lastModified}");
  }

  db.config.save(maxts);
}

def post(rec) {
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
                      'xmlns:xsi' : ' http://www.w3.org/2001/XMLSchema-instance') {
    'DC.Title'('title')
    'DC.Identifier'('title')
    'Description' {
      'DC.DESCRIPTION'(format:'plain','desctext')
    }
    'DCTerns'('title')
    'DC.Subject'('title')
    'DC.Creator'('title')
    'DC.Publisher'('href':'pubhref','title')
    'DC.Date.Created'('title')
    'DC.Date.Modified'('title')
    'ProviderDetails' {
      'ProviderName'()
      'ConsentVisibleAddress'(true)
      'SettingDetails' {
        'TelephoneNumber' {
          'apd:TelNationalNumber'('value')
        }
        'PostalAddress' {
          'apd:A_5LineAddress' {
            'apd:Line'()
            'apd:Line'()
            'apd:PostCode'()
          }
        }
      }
      'ChildcareType'() // Creche,...
      'ProvisionType'() // CCN,
      'ChildcareAges'() // CCN,
      'Country'('United Kingdom') // United Kingdom
      'ModificationDate'('')
      'RegistrationDetails'(RegistrationId:'') {
        'RegistrationDate'()
        'RegistrationConditions'()
        'RegistrationTypes' {
          'RegistrationType'() // VCR
        }
        'RegistrationStatus' {
          'RegistrationStatus'('ACTV') // ACTV
          'RegistrationStatusStartDate'('') // YYYY-MM-DD
        }
        'LastInspection' {
          'InspectionType'()
          'InspectionDate'()  // yyyy-mm-dd
          'InspectionOverallJudgementDescription'()
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
      'OfstedURN'(205685)
    }
  }

  def result = writer.toString();
  println(result);

}

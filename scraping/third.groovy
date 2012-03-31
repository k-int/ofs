#!/usr/bin/groovy

@Grapes([
  @Grab(group='com.gmongo', module='gmongo', version='0.9.2'),
  @Grab( 'org.ccil.cowan.tagsoup:tagsoup:1.2.1' )
])

import org.ccil.cowan.tagsoup.Parser


println "Open mongo";
def mongo = new com.gmongo.GMongo()
def db = mongo.getDB("ofs_source_reconcilliation")

println "Grab page...";
go(db);

db.close();


def go(db) {
  def gHTML = new URL( 'http://www.ofsted.gov.uk/inspection-reports/find-inspection-report' ).withReader { r ->
    new XmlSlurper( new Parser() ).parse( r )
  }

  println "Find select elements with right id...";
  
  // Look for  <select name="localDrp" class="form-select" id="edit-localDrp" >
  // def allLinks = gHTML.body.'**'.findAll { it.name() == 'select' && it.@id.text() == 'edit-localDrp' }
  def sel = gHTML.body.'**'.find { it.name() == 'select' && it.@id.text() == 'edit-localDrp' }

  // allLinks.each { link ->
  //   println link
  // }

  println "Listing..."

  sel.option.each { so ->
    println "processing authority with code ${so.@value.text()} ${so.text()}"
  
    def authid = so.@value.text()
  
    def authority_info = db.ofstedauth.findOne(authcode:authid);
    if ( authority_info == null ) {
      println("New record for authority ${authid}");
      authority_info = [:]
      authority_info.authcode = authid;
      authority_info.lastCheck = 0;
      db.ofstedauth.save(authority_info);
    }
    else {
      println("update existing record for ${authid}");
    }
  }

  db.ofstedauth.find().sort(lastCheck:1).each { authority_info ->

    def pageno = 0;
    def rectype = "16";
    def moredata = true
    println("Processing ${authority_info.authcode} last checked on ${authority_info.lastCheck}");

    while ( ( moredata ) && ( pageno < 100 ) ) {
      def next_page_url = "http://www.ofsted.gov.uk/inspection-reports/find-inspection-report/results/type/${rectype}/authority/${authority_info.authcode}/any/any?page=${pageno}"
      println "Collecting [${authority_info.authcode}][${pageno}]${next_page_url}"
  
      def next_page = new URL( next_page_url ).withReader { r ->
        new XmlSlurper( new Parser() ).parse( r )
      }
  
      // println next_page
  
      // def results_list = next_page.body.'**'.findAll { it.name() == 'ul' && it.@class.text() == 'resultsList' }
      def results_list = next_page.body.'**'.find { it.name() == 'ul' && it.@class.text() == 'resultsList' }
  
      // println "Results of results_list = ${results_list}"
  
      if ( results_list != null ) {
        println("got results list.... page=${pageno}")
        results_list.li.each {
          def new_prov_url = it.h2.a.@href.text();
          processProvider("http://www.ofsted.gov.uk${new_prov_url}",db,authority_info.authcode);
        }
        pageno++;
      }
      else {
        moredata = false;
      }
    }

    authority_info.lastCheck = System.currentTimeMillis();
    db.ofstedauth.save(authority_info);
  }

  println "Done"
}

def processProvider(provurl,db,authid) {

  println("Processing ${provurl}")



  def prov_page = new URL( provurl ).withReader { r ->
    new XmlSlurper( new Parser() ).parse( r )
  }


  def details = prov_page.'**'.find{ it.name() == 'div' && it.@id.text() == 'content' }

  if ( details != null ) {

    def ofsted_id = details.p[0].strong.text();
    def result = db.ofsted.findOne(ofstedId:ofsted_id)
    if ( result == null ) {
      result=[:]
      result.ofstedId = ofsted_id
      println("create ${ofsted_id}");
    }
    else {
      println("update ${ofsted_id}");
    }

    result.authority = authid
    result.uri = provurl
    result.name = details.h1.text();

    // println "Provider name: ${details.h1.text()}"
    // details.p.each {
    //   println "* ${it.text()}"
    // }
    // println "Address will be ${details.p[1].text()}"
    def addr_components = []
    details.p[1].getAt(0).children.each { addrcomp ->
      if (!(addrcomp instanceof groovy.util.slurpersupport.Node)) {
        addr_components.add(addrcomp.toString());
      }
      else {
      }
    }

    def contact_number_components = []
    details.p[2].getAt(0).children.each { cn ->
      if (!(cn instanceof groovy.util.slurpersupport.Node)) {
        contact_number_components.add(cn.toString().trim());
      }
      else {
      }
    }

    // Locate the date of registration..
    def regnode = details.'**'.find { it.name() == 'strong' && it.text() == 'Date of registration:' }
    if ( ( regnode != null ) && ( regnode.parent().text().length() > 22 ) ) {
      // println "regnode = ${regnode.parent().text()}"
      result.regdate = regnode.parent().text().substring(22);
    }

    // regtype
    def regtype = details.'**'.find { it.name() == 'strong' && it.text() == 'Register type:' }
    if ( regtype ) {
      result.regtype = regtype.parent().text().substring(15);
    }

      

    // Find conditions... "The registered person:"
    def trp_para = details.'**'.find { it.name() == 'p' && it.text() == 'The registered person:' }
    if ( trp_para ) {
      def trp_div = trp_para.parent()
      def trp_ul = trp_div.find { it.name() == 'ul' }
      result.conditions = []
      trp_ul?.li.each { cond ->
        result.conditions.add(cond.text())
      }
    }

    result.reports = []
    // Previous reports
    def prev_rep_tab = details.'**'.find { it.name() == 'table' && it.@summary.text() == 'Previous reports' }
    if ( prev_rep_tab ) {
      prev_rep_tab.tbody.tr.each { pr ->
        if ( pr.td[0] ) {
          result.reports.add([uri:pr.td[0].a.@href.text(),
                               description:pr.td[0].text(),
                               inspdate:pr.td[1].text(),
                               pubdate:pr.td[2].text()]);
        }
      }
    }

    // println "Addr components: ${addr_components}";
    // println "Contact number components: ${contact_number_components}";

    result.address = addr_components
    result.contact = contact_number_components
    result.postcode = addr_components[addr_components.size()-1]
    result.lastModified = System.currentTimeMillis();

    println(result);

    db.ofsted.save(result);
  }
  else {
    // println "No div with id middleColumn";
  }

}

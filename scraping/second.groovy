#!/usr/bin/groovy


@Grab( 'org.ccil.cowan.tagsoup:tagsoup:1.2.1' )
import org.ccil.cowan.tagsoup.Parser


println "Grab page...";

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
  def pageno = "0";
  def rectype = "16";
  def moredata = true

  while ( ( moredata ) && ( pageno < 100 ) ) {
    def next_page_url = "http://www.ofsted.gov.uk/inspection-reports/find-inspection-report/results/type/${rectype}/authority/${authid}/any/any?page=${pageno}"
    println "Collecting ${next_page_url}"

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
        processProvider("http://www.ofsted.gov.uk${new_prov_url}");
      }
      pageno++;
    }
    else {
      moredata = false;
    }
  }
}

println "Done"


def processProvider(provurl) {
  println("Processing ${provurl}")
  def prov_page = new URL( provurl ).withReader { r ->
    new XmlSlurper( new Parser() ).parse( r )
  }

  def details = prov_page.'**'.find{ it.name() == 'div' && it.@id.text() == 'content' }

  if ( details != null ) {
    println "Provider name: ${details.h1.text()}"
    // details.p.each {
    //   println "* ${it.text()}"
    // }
    println "Address will be ${details.p[1].text()}"
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
    println "regnode = ${regnode.parent().text()}"

    println "Addr components: ${addr_components}";
    println "Contact number components: ${contact_number_components}";
  }
  else {
    println "No div with id middleColumn";
  }
}

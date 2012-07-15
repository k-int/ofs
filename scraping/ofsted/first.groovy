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
  println so
  println "${so.@value.text()} ${so.text()}"

  def authid = so.@value.text()
  def pageno = "0";
  def rectype = "16";
  def moredata = true
  while ( ( moredata ) && ( count < 200 ) ) {
    def next_page_url = "http://www.ofsted.gov.uk/inspection-reports/find-inspection-report/results/type/${rectype}/authority/${authid}/any/any?page=${pageno}"

    println "Collecting ${next_page_url}"

    def next_page = new URL( next_page_url ).withReader { r ->
      new XmlSlurper( new Parser() ).parse( r )
    }

    def results_list = next_page.find { it.name() == 'ul' && it.@class.text() == 'resultsList' }

    if ( results_list != null ) {
      println("got results list...."
    }
  }

  // Each page contains a <ul class="resultsList">, the last page doesn't


}

println "Done"

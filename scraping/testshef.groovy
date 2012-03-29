#!/usr/bin/groovy


@Grab( 'org.ccil.cowan.tagsoup:tagsoup:1.2.1' )
import org.ccil.cowan.tagsoup.Parser


def authid = "373"
def pageno = 20;
def rectype = "16";
def moredata = true
while ( ( moredata ) && ( pageno < 25 ) ) {
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
      println ("${it.h2.a.@href}");
    }
    pageno++;
  }
  else {
    moredata = false;
  }
}

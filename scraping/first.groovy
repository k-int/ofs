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
  println "${so.value} ${so.text()}"
}

println "Done"

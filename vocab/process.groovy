#!/usr/bin/groovy

@Grab( 'xalan:xalan:2.6.0' )

import org.apache.xpath.XPathAPI
import javax.xml.parsers.DocumentBuilderFactory


// Code example from http://stackoverflow.com/questions/4744546/groovy-edit-xml-file-keep-comments-line-breaks
// def doc = DOMBuilder.parse(new StringReader(input))
// def root = doc.documentElement
// use(groovy.xml.dom.DOMCategory) {
//     def chocolate = root.depthFirst().grep{it.text() == "Chocolate"}
//     chocolate*.value = "Nutella"
// }
// 
// def result = groovy.xml.dom.DOMUtil.serialize(root)
// println result


println("Load");
def xmldoc = load('./fidy.xml');

XPathAPI.selectNodeList(xmldoc, '//term').each{ processTerm(xmldoc, it) }

def result = groovy.xml.XmlUtil.serialize(xmldoc)
println result

println("Done");

def load(filename) {
  def builder     = DocumentBuilderFactory.newInstance().newDocumentBuilder()
  def inputStream = new FileInputStream(new File(filename))
  def records     = builder.parse(inputStream).documentElement
  records
}

def processTerm(doc, term) {
  def term_id = XPathAPI.eval(term, './termId/text()').str()
  def new_node = doc.getOwnerDocument().createElement('scopeNote')
  new_node.appendChild(doc.getOwnerDocument().createTextNode("Records matching this query can be found at http://www.openfamilyservices.org.uk/ofs/?keywords=&amp;subject=${term_id}"))
  term.appendChild(new_node);
  // println("Process term ${term_id}");
}

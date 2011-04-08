package ofs

class OfsTagLib {

  def outputSolrProperty = { attrs, body ->
    if ( attrs.prop != null ) {
      if ( attrs.prop instanceof java.util.List ) {
        attrs.prop.each {
          out << "<li>${it}</li>"
        }
      }
      else {
        out << "<li>${attrs.prop}</li>"
      }
    }
  }
}

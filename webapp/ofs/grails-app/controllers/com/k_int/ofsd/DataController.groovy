package com.k_int.ofsd

import grails.converters.*
import com.k_int.iep.datamodel.*

class DataController {

  def orgs() { 
    def orgs = []
    IEPProvider.findAll().each { o ->
      orgs.add([
                identifier:o.identifier,
                shortCode:o.shortCode,
                name:o.name,
                email:o.email,
                office:o.office,
                thoroughfare:o.thoroughfare,
                locality:o.locality,
                dependentLocality:o.dependentLocality,
                region:o.region,
                postcode:o.postcode,
                description:o.description,
                contactEmail:o.contactEmail,
                contactTelephone:o.contactTelephone,
                contactFax:o.contactFax,
                sourceDisclaimer:o.sourceDisclaimer,
                iconURL:o.iconURL,
                showLogo:o.showLogo,
                subType:o.subscriptionType?.code])
    }

    withFormat {
      json { render orgs as JSON }
      xml { render orgs as XML }
    }
  }
}

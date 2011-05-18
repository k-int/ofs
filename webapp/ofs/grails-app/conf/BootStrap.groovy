import com.k_int.iep.services.*;

import org.codehaus.groovy.grails.commons.ApplicationHolder

class BootStrap {

    def dppSyncService

    def init = { servletContext ->

      println "dpp apikey is ${ApplicationHolder.application.config.ofs.dpp.apikey}"

      // if this application has a dpp import context defined, call the sync routines
      if ( dppSyncService != null ) {
        println "dppSyncService is present.. checking for dppImportContext config"
        if ( ApplicationHolder.application.config.ofs?.dpp?.sync_contexts != null ) {
          println "Sync contexts located in app config"
          ApplicationHolder.application.config.ofs.dpp.sync_contexts.each { sc ->
            println "Sync with ${sc.value.appname} - ${sc.value.connect_string}"
            dppSyncService.syncWithDpp(sc.value.appname, 
                                       sc.value.jdbc_driver, 
                                       sc.value.connect_string,
                                       sc.value.user,
                                       sc.value.pass)
          }
        }
        else {
          println "No sync contexts found"
        }
      }
      else {
        println "No dppSyncService"
      }
    }
    def destroy = {
    }
}

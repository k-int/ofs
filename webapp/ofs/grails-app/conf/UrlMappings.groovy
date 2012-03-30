class UrlMappings {

  static mappings = {


    // Useful resource on error trapping
    // http://groovy.dzone.com/articles/grails-exception-handling-http

    // "/$controller/$action?/$id?"{
    //    constraints {
      // apply constraints here
    //    }
    //  }

    "/about" (controller:"about", action:"index")

    "/jcaptcha/$action?/$id?" (controller:"jcaptcha")

    "/sitemap" (controller:"siteMapindex", action:"siteindex")
    "/directory/$authority" (controller:"search", action:"search")
    "/directory/$authority/sitemap" (controller:"siteMapindex", action:"authsitemap")
    // "/directory/$authority/stats" (controller:"authority", action:"stats")
    "/directory/$authority/$id/feedback" (controller:"entry", action:"feedback")
    "/directory/$authority/$id" (controller:"entry", action:"index")
    "/data/$action" (controller:"data")

    "/gaz" (controller:"gaz", action:"index")

    "/" (controller:"search", action:"search")

    "500"(view:'/error')
    "404"(view:'/notfound')
    "410"(view:'/notfound')
  }
}

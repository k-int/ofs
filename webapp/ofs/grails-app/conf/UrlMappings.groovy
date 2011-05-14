class UrlMappings {

  static mappings = {


    // "/$controller/$action?/$id?"{
    //    constraints {
      // apply constraints here
    //    }
    //  }

    "/about" (controller:"about", action:"index")

    "/jcaptcha/$action?/$id?" (controller:"jcaptcha")
    "/sitemap" (controller:"siteMapindex", action:"siteindex")
    "/directory/$authority/sitemap" (controller:"siteMapindex", action:"authsitemap")
    "/directory/$authority/$id/feedback" (controller:"entry", action:"feedback")
    "/directory/$authority/$id" (controller:"entry", action:"index")

    "/gaz" (controller:"gaz", action:"index")

    "/" (controller:"search", action:"search")

    "500"(view:'/error')
  }
}

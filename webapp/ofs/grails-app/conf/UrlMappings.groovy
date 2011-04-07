class UrlMappings {

  static mappings = {


    // "/$controller/$action?/$id?"{
    //    constraints {
      // apply constraints here
    //    }
    //  }

    "/directory/$id" (controller:"entry", action:"index")

    "/" (controller:"search", action:"search")

    "500"(view:'/error')
  }
}

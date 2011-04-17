<html>
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
    <!-- Include the main layout from the grails-app/views/layouts dir - Thats where are the styles etc are imported -->
    <meta name="layout" content="main" />
    <title>OFS Search</title>
  </head>
  <body>

    <div class="yui3-g"> 
      <div class="yui3-u" style="width:100%"> 
        <form action="/ofs/" method="get" > 
          <div class="search-box"> 
            OFS <input class="uiw-input" type="text" name="q"/><input class="uiw-button" value="Search" type="submit"/> 
          </div> 
        </form> 
      </div> 
    </div>    

  </body>
</html>

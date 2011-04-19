<html>
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
    <!-- Include the main layout from the grails-app/views/layouts dir - Thats where are the styles etc are imported -->
    <meta name="layout" content="main" />
    <title>OFS Search</title>
  </head>
  <body class="search-results">


    <div class="yui3-g"> 

      <img src="${resource(dir:'images',file:'ofs_logo_1.svg')}" width="250" height="250"/>

      <div class="yui3-u" style="width:100%"> 
        <form action="/ofs/" method="get" > 
          <div class="search-box"> 
            <g:message code="ofs.search.prompt"/> <input class="uiw-input" type="text" name="q"/><input class="uiw-button" value="Search" type="submit"/> 
          </div> 
        </form> 
      </div> 
    </div>    

  </body>
</html>

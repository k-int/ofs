<html>
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
    <!-- Include the main layout from the grails-app/views/layouts dir - Thats where are the styles etc are imported -->
    <meta name="layout" content="main" />
    <title>OFS Search</title>
  </head>
  <body class="search-results">


    <div class="yui3-g"> 

      <div class="splash"></div>

      <div class="yui3-u" style="width:100%"> 
        <form action="/ofs/" method="get" > 
          <div class="search-box"> 
            <g:message code="ofs.search.prompt"/> <input class="uiw-input" type="text" name="q"/><input class="uiw-button" value="Search" type="submit"/> 
          </div> 
        </form> 
      </div> 
    </div>    

    <div class="footerlinks">
      &nbsp;<br/>
      &nbsp;<br/>
      &nbsp;<br/>
      <a href="http://partners.openfamilyservices.org.uk/?page_id=9">About</a>
      <a href="http://partners.openfamilyservices.org.uk/?page_id=9">Local Authorties</a>
      <a href="http://partners.openfamilyservices.org.uk/?page_id=9">Channel Partners</a>
    </div>
  </body>
</html>

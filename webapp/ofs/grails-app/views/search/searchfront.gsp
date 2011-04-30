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
      <!--
      <div class="maintitle">&nbsp;<br/>OpenFamilyServices</div>
      <div class="subtitle">&nbsp;<br/>Local Family Services - National Coverage</div>
      -->

      <div class="preamble"> <!-- Search preamble -->
        &nbsp;<br/>&nbsp;<br/>
        Enter your postcode, town, village or street into the search box below, and get instant access to details of your local services and activities.
      </div>

      <div class="yui3-u" style="width:100%"> 
        <form action="/ofs/" method="get" > 
          <div class="search-box"> 
            <g:message code="ofs.search.prompt"/> <input class="uiw-input" type="text" name="placename"/><input class="uiw-button" value="Search" type="submit"/> 
          </div> 
        </form> 
      </div> 
      <div class="yui3-u" style="width:100%"> 
OpenFamilyServices is the national online directory where you can search for and find information on a wide range of family-related services.  The information comes from Local Authorities across England. Visitors can search for information about national services, and services and service providers within their own locality, or further afield, through this single portal.
      </div>
    </div>    

    <div class="footerlinks yui3-u">
      &nbsp;<br/>
      &nbsp;<br/>
      &nbsp;<br/>
      &nbsp;<br/>
      &nbsp;<br/>
      <a href="http://partners.openfamilyservices.org.uk/?page_id=9">About</a>
      <a href="http://partners.openfamilyservices.org.uk/?page_id=9">Local Authorities</a>
      <a href="http://partners.openfamilyservices.org.uk/?page_id=9">Channel Partners</a> <br/>

      <div id="google_translate_element"></div><script>
      function googleTranslateElementInit() {
        new google.translate.TranslateElement({
          pageLanguage: 'en',
          layout: google.translate.TranslateElement.InlineLayout.HORIZONTAL
        }, 'google_translate_element');
      }
      </script><script src="//translate.google.com/translate_a/element.js?cb=googleTranslateElementInit"></script> 
    </div>
  </body>
</html>

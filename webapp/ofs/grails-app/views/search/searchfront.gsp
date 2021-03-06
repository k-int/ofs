<html>
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
    <!-- Include the main layout from the grails-app/views/layouts dir - Thats where are the styles etc are imported -->
    <meta name="layout" content="main" />
    <title>OFS Search</title>
  </head>

  <body class="search-results">

<div style="float:right">
   <!-- AddThis Button BEGIN -->
    <div class="addthis_toolbox addthis_default_style ">
    <a class="addthis_button_preferred_1" style="float: left;"></a>
    <a class="addthis_button_preferred_2" style="float: left;"></a>
    <a class="addthis_button_preferred_3" style="float: left;"></a>
    <a class="addthis_button_preferred_4" style="float: left;"></a>
    <a class="addthis_button_compact" style="float: left;"></a>
    <a class="addthis_counter addthis_bubble_style" style="float: left;"></a>
    </div>
    <script type="text/javascript">var addthis_config = {"data_track_clickback":true};</script>
    <script type="text/javascript" src="http://s7.addthis.com/js/250/addthis_widget.js#pubid=${grailsApplication.config.ofs.addthis.code}"></script><br/>
    <!-- AddThis Button END -->
</div>



    <div class="yui3-g"> 

      <div class="splash"></div>

      <div class="preamble"> <!-- Search preamble -->
        &nbsp;<br/>&nbsp;<br/>
        Enter your postcode (full postcodes work best), town or village into the search box below, along with any optional keywords and get instant access to details of your local services and activities.
      </div>

      <div class="yui3-u" style="width:100%"> 
        <form method="get" > 
          <div class="search-box">
              <div class="srchprompt"></div><input class="uiw-input2" type="text" name="q"/><input class="uiw-button" value="Search" type="submit"/> 
          </div> 
        </form> 
      </div> 
      <div class="yui3-u" style="width:100%; color: #669933; font-style: italic; margin-top: 15px; clear:both;"> 
OpenFamilyServices is the national online directory where you can search for and find information on a wide range of family-related services.  The information comes from Local Authorities across England. Visitors can search for information about national services, and services and service providers within their own locality, or further afield, through this single portal.
      </div>
    </div>    

    <div class="footerlinks yui3-u" style="margin-top:30px">
      <a href="http://partners.openfamilyservices.org.uk/?page_id=9">About</a>
      <a href="http://partners.openfamilyservices.org.uk/?page_id=9">Local Authorities</a>
      <a href="http://partners.openfamilyservices.org.uk/?page_id=9">Channel Partners</a>
      <a href="http://partners.openfamilyservices.org.uk/?page_id=113">Information Consent/Privacy</a> <br/>

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

<html>
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
    <!-- Include the main layout from the grails-app/views/layouts dir - Thats where are the styles etc are imported -->
    <meta name="layout" content="main" />

    <g:if test="${params.authority != null}">
      <title><g:message code="ofs.search.title"/> - <g:message code="cv.authority_shortcode.${params.authority}"/></title>
    </g:if>
    <g:else>
      <title><g:message code="ofs.search.title"/></title>
    </g:else>

    <script src="http://yui.yahooapis.com/3.3.0/build/yui/yui-min.js" charset="utf-8"></script>

  </head>

  <body class="search-results yui3-skin-sam">

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
    <script type="text/javascript" src="http://s7.addthis.com/js/250/addthis_widget.js#pubid=ra-4dbbf52024003ac1"></script><br/>
    <!-- AddThis Button END -->
</div>



    <div class="yui3-g"> 

      <div class="splash"></div>

      <div class="yui3-u" style="width:100%; color: #669933; font-style: italic; margin-top: 15px; clear:both;">
OpenFamilyServices is a national online directory of accredited family services, information, childcare and positive activities. The information comes directly from participating Local Authorities across England who verify the information to ensure it's accuracy. Information about national services is also available in a single search.
      </div>
      <!-- For alerts
      <div id="alertspace" class="greenpane">
      </div>
      -->

      <g:if test="${params.authority != null}">
        <div class="preamble"><h2>Search records from <g:message code="cv.authority_shortcode.${params.authority}"/> Only</h2><hr/></div>
      </g:if>
      <div class="preamble"> <!-- Search preamble -->
        &nbsp;<br/>&nbsp;<br/>
        Enter your postcode, town, village or street into the place search box below, and get instant access to details of your local services and activities.<br/> 
        Add keywords to the keywords box to refine your search.
      </div>

      <div class="yui3-u" style="width:100%"> 
        <form method="get" > 
          <div class="search-box">
            <div class="searchrow"> 
              <g:message code="ofs.search.keywordprompt"/> <input class="uiw-input" type="text" name="keywords"/><input class="uiw-button" value="Search" type="submit"/> 
            </div>
            <div class="searchrow" style="padding-top:10px"> 
              <g:message code="ofs.search.placeprompt"/> <input id="place-input" class="uiw-input" type="text" name="placename"/><input class="uiw-button" value="Search" type="submit"/> 
            </div>
            &nbsp;
          </div> 
        </form> 
      </div> 


    </div>    

    <div class="footerlinks yui3-u" style="margin-top:30px">
      <a href="http://partners.openfamilyservices.org.uk/?page_id=9">About</a>
      <a href="http://partners.openfamilyservices.org.uk/?page_id=9">Local Authorities</a>
      <a href="http://partners.openfamilyservices.org.uk/?page_id=9">Channel Partners</a>

      <div id="google_translate_element"></div><script>
      function googleTranslateElementInit() {
        new google.translate.TranslateElement({
          pageLanguage: 'en',
          layout: google.translate.TranslateElement.InlineLayout.HORIZONTAL
        }, 'google_translate_element');
      }
      </script><script src="//translate.google.com/translate_a/element.js?cb=googleTranslateElementInit"></script> 
    </div>

    <script>
      YUI().use("autocomplete", function (Y) {
        Y.one('#place-input').plug(Y.Plugin.AutoComplete, {
          // resultHighlighter: 'phraseMatch',
          resultListLocator: 'results',
          resultTextLocator: 'fqn',
          maxResults:6,
          source: '${grailsApplication.config.ofs.frontend}/ofs/gaz?q={query}&callback={callback}'
        });
      });
    </script>

  </body>
</html>

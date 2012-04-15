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

    <link rel="canonical" href="http://www.openfamilyservices.org.uk/ofs" />

  </head>

  <body class="search-results yui3-skin-sam">

    <div class="yui3-g main"> 
      <div class="yui3-u-3-4">

        <div class="splash"></div>
  
        <g:if test="${params.authority != null}">
          <div class="preamble">
            <h1>Search the Directory of Family Services Registered Childminders &amp; Nurseries</h1>
            <h2><g:message code="cv.authority_shortcode.${params.authority}"/></h2>
          </div>
        </g:if>
        <g:else>
          <div class="preamble"><h1>Search the Directory of Family Services Registered Childminders &amp; Nurseries</h1></div>
        </g:else>
       
        <div class="preamble"> <!-- Search preamble -->
  Simply enter 1 or 2 ‘keywords’ in the search box below about the service you need and either your postcode, town, or street name into the ‘place’ search box and press either ‘search’ to get instant access to details of your local services and activities.<br/>
  eg Nursery &nbsp; 1XY
        </div>
  
        <div>
          <form method="get" > 
            <div class="search-box">
              <div class="searchrow"> 
                <g:message code="ofs.search.keywordprompt"/> <input class="uiw-input" type="text" name="keywords"/><input class="uiw-button" value="Search" type="submit"/> 
              </div>
              <div class="searchrow" style="padding-top:10px"> 
                <g:message code="ofs.search.placeprompt"/> <input id="place-input" 
                                                                  class="uiw-input" 
                                                                  type="text" 
                                                                  name="placename"
                                                                  style="text-transform: capitalize;"/>
                                                           <input class="uiw-button" value="Search" type="submit"/> 
              </div>
              &nbsp;
            </div> 
          </form> 
        </div> 

        <div style="padding:30px; text-align:justify;"><p>OFS is a national online directory of family services containing information about Ofsted registered Childcare such as Nurseries, Pre-schools, Childminders and Out of School Clubs. There are also many Support Groups, Parent &amp; Toddler Groups, Children’s Centre Activities and Targeted Services available to help with family life;  as well as things to do for Leisure and Holiday Activities. The information comes directly from participating Local Authorities across England who verify the information to ensure its accuracy.  Information about national services is also available in a single search.</p>
        <p>Want to chat? Join our family forum at <a href="http://www.openfamilyforum.org.uk"/>OpenFamilyForum</a>. Want to find contact details for your local Family Information Service? You can <a href="http://www.openfamilyforum.org.uk/fis-map.html">View the map of UK Family Information Service departments</a> </p>
        </div>
      </div>    

      <div class="yui3-u-1-4">
        <div style="padding:10px;">
<script type="text/javascript"><!--
google_ad_client = "${grailsApplication.config.ofs.adsense.clientcode}";
/* OFSRight1 */
google_ad_slot = "${grailsApplication.config.ofs.adsense.adslot}";
google_ad_width = 120;
google_ad_height = 600;
//-->
</script>
<script type="text/javascript"
src="http://pagead2.googlesyndication.com/pagead/show_ads.js">
</script>
        </div>
      </div>
    </div> <!-- end yui3-g main-->

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

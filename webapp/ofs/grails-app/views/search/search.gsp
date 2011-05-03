<html>
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
    <link rel="stylesheet" href="site_files/reset.css" type="text/css"/> 
    <link rel="stylesheet" href="site_files/fonts.css" type="text/css"/> 
    <link rel="stylesheet" href="site_files/base.css" type="text/css"/> 
        
    <link rel="stylesheet" type="text/css" href="http://yui.yahooapis.com/3.3.0/build/cssgrids/grids-min.css"> 
 
    <!-- Include the main layout from the grails-app/views/layouts dir - Thats where are the styles etc are imported -->
    <meta name="layout" content="searchresultsmain" />
    
    <title><g:message code="ofs.search.title"/></title>
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
    <script type="text/javascript" src="http://s7.addthis.com/js/250/addthis_widget.js#pubid=ra-4dbbf52024003ac1"></script><br/>
    <!-- AddThis Button END -->
</div>


    <div class="yui3-g"> 
      <div class="yui3-u" style="width:100%"> 
        <div class="yui3-g"> 
          <div class="yui3-u" style="width:100%"> 
            <form action="/ofs/" method="get" > 
              <div class="search-box"> 
                <g:message code="ofs.search.prompt"/>  <input class="uiw-input" type="text" name="placename" value="${params.placename}" /><input class="uiw-button" value="Search" type="submit"/> 
                <g:if test="${params.subject != null || params.flags != null || params.authority != null || params.restp != null}">
                  <ul style="display:inline" id="activefilters">
                    <g:if test="${params.restp != null}"><li style="display:inline"><g:message code="cv.restp"/>: <g:message code="cv.restp.${params.restp}"/></li></g:if>
                    <g:if test="${params.subject != null}"><li style="display:inline"><g:message code="cv.dc.subject.orig_s"/>: <g:message code="cv.dc.subject.orig_s.${params.subject}"/></li></g:if>
                    <g:if test="${params.flags != null}"><li style="display:inline"><g:message code="cv.flags"/>: <g:message code="cv.flags.${params.flags}"/></li></g:if>
                    <g:if test="${params.authority != null}"><li style="display:inline"><g:message code="cv.authority_shortcode"/>: <g:message code="cv.authority_shortcode.${params.authority}"/></li></g:if>
                  </ul>
                </g:if>
              </div> 
            </form> 
            <a href="/ofs">Home</a>
          </div> 
        </div>  
      </div> 
    </div> 
  
    <div class="yui3-g"> 
      <div class="yui3-u-19-24" style="float:right"> 
        <div id="SearchResults"> 

<g:if test="${noqry != true}">
  <div class="searchflash">
    ${search_results.results.numFound} records found in ${elapsed} seconds
  </div>
  <ul>
    <g:each status="s" in="${search_results.results}" var="sr">

      <li><!--[${s+search_results.results.start}]-->
        <g:if test="${(sr['icon_url_s'] != null ) && ( sr['icon_url_s'].length() > 0 )}"><img src="${sr['icon_url_s']}" style="float:right"/></g:if>
        <ul>

          <li><strong><a href="/ofs/directory/${sr['authority_shortcode']}/${sr['aggregator.internal.id']}">${sr['dc.title']}</a></strong> <g:if test="${( sr['childcare_type_s'] != null )}">${sr['childcare_type_s']}</g:if></li>


          <g:if test="${(sr['dc.description'] != null ) && ( sr['dc.description'].length() > 0 )}"><li>${sr['dc.description']}</li></g:if>

          <li>Address:
            <g:if test="${(sr['address.line1'] != null ) && ( sr['address.line1'].length() > 0 )}">${sr['address.line1']}</g:if>
            <g:if test="${(sr['address.line2'] != null ) && ( sr['address.line2'].length() > 0 )}">, ${sr['address.line2']}</g:if>
            <g:if test="${(sr['address.line3'] != null ) && ( sr['address.line3'].length() > 0 )}">, ${sr['address.line3']}</g:if>
            <g:if test="${(sr['address.line4'] != null ) && ( sr['address.line4'].length() > 0 )}">, ${sr['address.line4']}</g:if>
            <g:if test="${(sr['address.line5'] != null ) && ( sr['address.line5'].length() > 0 )}">, ${sr['address.line5']}</g:if>
          </li>
          <g:if test="${sr['distance'] != null}"><li>Approximately ${ ((int)Math.abs( sr['distance'] * 100 ))/100 } - miles from ${params.placename}</li></g:if>

          <g:if test="${(sr['telephone'] != null ) && ( sr['telephone'].length() > 0 )}"><li>Telephone: ${sr['telephone']}</li></g:if>
          <g:if test="${(sr['email'] != null ) && ( sr['email'].length() > 0 )}"><li>Email: <a href="mailto:${sr['email']}">${sr['email']}</a></li></g:if>

          <g:if test="${( sr['modified'] != null )}">
	    <li>Last modified: ${sr['modified'].substring(0,sr['modified'].indexOf("T"))}</li>
          </g:if>

          <g:if test="${( sr['feedback_url_s'] != null )}">
            <g:if test="${(sr['feedback_url_s'].indexOf('@') > 0)}">
              <li>Via: <a href="mailto:${sr['feedback_url_s']}">${sr['feedback_name_s']}</a></li>
            </g:if>
            <g:else>
              <li>Via: <a href="http://${sr['feedback_url_s']}">${sr['feedback_name_s']}</a></li>
            </g:else>
          </g:if>

        </ul>
      </li>
    </g:each>
  </ul>
</g:if>
<g:else>
  No query, show the search form.
</g:else>


          <div id="pagination"> 
            <g:paginate next="Forward" prev="Back" maxsteps="0" controller="search" action="search" total="${search_results.results.numFound}" params="${params}"> </g:paginate>
          </div> 
        </div>
      </div>

    </div>
                
    <div class="yui3-u-5-24"> 
      
      <div id="facets">
        <h2>Refine search by...</h2>
        <g:if test="${((search_results != null) && (search_results.facetFields != null))}">
          <g:each in="${search_results.facetFields}" var="fl">

            <%
              request.setAttribute("ops", params.clone())
            %>

            <div class="facet"><g:message code="cv.${fl.name}"/>
              <ul>
                <g:each in="${fl.values}" var="flv">

                  <% request.ops."${facetFieldMappings[fl.name] ?: fl.name}" = "${flv.name}" %>

                  <li><g:link action="search" controller="search" params="${request.ops}"><g:message code="cv.${fl.name}.${flv.name}"/></g:link> (${flv.count})</li>
                </g:each>
              </ul>
            </div>
          </g:each>
        </g:if>
        <g:else>
          No facets....
        </g:else>
      </div><!--SearchResults-->
    </div>

    <div class="footerlinks yui3-u">
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

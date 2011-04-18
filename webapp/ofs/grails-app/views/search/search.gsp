<html>
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
    <link rel="stylesheet" href="site_files/reset.css" type="text/css"/> 
    <link rel="stylesheet" href="site_files/fonts.css" type="text/css"/> 
    <link rel="stylesheet" href="site_files/base.css" type="text/css"/> 
        
    <link rel="stylesheet" type="text/css" href="http://yui.yahooapis.com/3.3.0/build/cssgrids/grids-min.css"> 
 
    <!-- Include the main layout from the grails-app/views/layouts dir - Thats where are the styles etc are imported -->
    <meta name="layout" content="main" />
    
    <title>OFS Search</title>
  </head>
  <body class="search-results">

    <div class="yui3-g"> 
      <div class="yui3-u" style="width:100%"> 
        <div class="yui3-g"> 
          <div class="yui3-u" style="width:100%"> 
            <form action="/ofs/" method="get" > 
              <div class="search-box"> 
                <g:message code="ofs.search.prompt"/>  <input class="uiw-input" type="text" name="q" value="Childcare sheffield" /><input class="uiw-button" value="Search" type="submit"/> 
              </div> 
            </form> 
          </div> 
        </div>  
      </div> 
    </div> 
  
    <div class="yui3-g"> 
      <div class="yui3-u-19-24" style="float:right"> 
        <div id="SearchResults"> 

<g:if test="${noqry != true}">
  There was a query: ${qry}<br/>
  Search found ${search_results.results.numFound} records, showing items starting at ${search_results.results.start}.
  <ul>
    <g:each status="s" in="${search_results.results}" var="sr">

      <li><!--[${s+search_results.results.start}]-->
        <g:if test="${(sr['icon_url_s'] != null ) && ( sr['icon_url_s'].length() > 0 )}"><img src="${sr['icon_url_s']}" style="float:right"/></g:if>
        <ul>

          <li><strong><a href="/ofs/directory/${sr['authority_shortcode']}/${sr['aggregator.internal.id']}">${sr['dc.title']}</a></strong></li>

          <g:if test="${(sr['dc.description'] != null ) && ( sr['dc.description'].length() > 0 )}"><li>${sr['dc.description']}</li></g:if>

          <li>Address:<br/>
            <g:if test="${(sr['address.line1'] != null ) && ( sr['address.line1'].length() > 0 )}"><li>${sr['address.line1']}</li></g:if>
            <g:if test="${(sr['address.line2'] != null ) && ( sr['address.line2'].length() > 0 )}"><li>${sr['address.line2']}</li></g:if>
            <g:if test="${(sr['address.line3'] != null ) && ( sr['address.line3'].length() > 0 )}"><li>${sr['address.line3']}</li></g:if>
            <g:if test="${(sr['address.line4'] != null ) && ( sr['address.line4'].length() > 0 )}"><li>${sr['address.line4']}</li></g:if>
            <g:if test="${(sr['address.line5'] != null ) && ( sr['address.line5'].length() > 0 )}"><li>${sr['address.line5']}</li></g:if>
          </li>

          <g:if test="${(sr['telephone'] != null ) && ( sr['telephone'].length() > 0 )}"><li>Telephone: ${sr['telephone']}</li></g:if>
          <g:if test="${(sr['email'] != null ) && ( sr['email'].length() > 0 )}"><li>Email: ${sr['email']}</li></g:if>

          <g:if test="${( sr['ispp.age_min'] != null ) && ( sr['ispp.age_max'] != null )}">
            <li>Age Range: from ${sr['ispp.age_min']} to ${sr['ispp.age_max']} years
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
            <g:paginate next="Forward" prev="Back" maxsteps="0" controller="search" action="search" total="${search_results.results.numFound}" params="[q:params.q]"> </g:paginate>
            <span class="currentStep">1</span><a href="/ofs/?q=Childcare+sheffield&amp;offset=10&amp;max=10" class="step">2</a><a href="/ofs/?q=Childcare+sheffield&amp;offset=20&amp;max=10" class="step">3</a><a href="/ofs/?q=Childcare+sheffield&amp;offset=30&amp;max=10" class="step">4</a><a href="/ofs/?q=Childcare+sheffield&amp;offset=40&amp;max=10" class="step">5</a><a href="/ofs/?q=Childcare+sheffield&amp;offset=50&amp;max=10" class="step">6</a><a href="/ofs/?q=Childcare+sheffield&amp;offset=10&amp;max=10" class="nextLink">Forward</a> 
          </div> 
        </div>
      </div>
    </div>
                
    <div class="yui3-u-5-24"> 
      <div id="facets">
        <g:if test="${((search_results != null) && (search_results.facetFields != null))}">
          <g:each in="${search_results.facetFields}" var="fl">
            <div class="facet">facet <g:message code="cv.${fl.name}"/>
              <ul>
                <g:each in="${fl.values}" var="flv">
                  <li><g:message code="cv.${fl.name}.${flv.name}"/> - ${flv.count}</li>
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

  </body>
</html>

<html>
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />

    <link rel="stylesheet" href="http://yui.yahooapis.com/3.4.1/build/cssreset/reset.css" type="text/css"/> 
    <link rel="stylesheet" href="http://yui.yahooapis.com/3.4.1/build/cssfonts/fonts.css" type="text/css"/> 
    <link rel="stylesheet" href="http://yui.yahooapis.com/3.4.1/build/cssbase/base.css" type="text/css"/>

    <link rel="stylesheet" href="${resource(dir:'css',file:'main.css')}" />
        
    <link rel="stylesheet" type="text/css" href="http://yui.yahooapis.com/3.4.1/build/cssgrids/grids-min.css"> 
    <script src="http://yui.yahooapis.com/3.4.1/build/yui/yui-min.js" charset="utf-8"></script>

    <!-- Include the main layout from the grails-app/views/layouts dir - Thats where are the styles etc are imported -->
    <meta name="layout" content="searchresultsmain" />

    <!-- Ask search engines not to index the search results pages, it looks horrible in google -->
    <meta name="robots" CONTENT="noindex, follow">
    
    <title> ${params.keywords} ${params.placename} <g:if test="${search_results.results.numFound==0}">-Nothing Found-</g:if> <g:if test="${params.authority != null}"> <g:message code="ofs.searchresults.title"/> (<g:message code="cv.authority_shortcode.${params.authority}"/>) </g:if><g:else> <g:message code="ofs.searchresults.title"/> </g:else> </title>

  </head>

  <body class="search-results yui3-skin-sam">
    <div id="navbarcontainer">
      <div id="navbar">
        OFS &gt; 
        <g:link controller="search" action="search">Home</g:link>
      </div>
    </div>


    <!-- Search bar -->
    <div class="yui3-g main"> 
      <div class="yui3-u" style="width:100%"> 
        <div class="yui3-g"> 
          <div class="yui3-u" style="width:100%"> 
            <form method="get">
              <div class="search-box">
                <g:message code="ofs.search.keywordprompt"/>:<input class="uiw-input" type="text" name="keywords" value="${params.keywords?.encodeAsHTML()}"/>&nbsp;&nbsp;&nbsp;
                <g:message code="ofs.search.placeprompt"/>:<input id="place-input" class="uiw-input" type="text" name="placename" value="${params.placename?.encodeAsHTML()}"/>

                <input class="uiw-button" value="Search" type="submit"/> 

                <g:if test="${params.subject != null || params.flags != null || params.authority != null || params.restp != null || place != null}">
                <font size="-1">
                  <ul class="cleanlist" id="activefilters">
                    <g:if test="${place != null}"><li class="cleanlist">Search Near "${place.fqn}".<br/> Guessed Place Wrong?  <g:link action="search" controller="search" params="${['keywords':params.q]}">Query again as keywords only</g:link></li></g:if>
                    <g:if test="${params.restp != null}"><li class="cleanlist"><g:message code="cv.restp"/>: <g:message code="cv.restp.${params.restp}"/></li></g:if>
                    <g:if test="${params.subject != null}"><li class="cleanlist"><g:message code="cv.dc.subject.orig_s"/>: <g:message code="cv.dc.subject.orig_s.${params.subject}"/></li></g:if>
                    <g:if test="${params.flags != null}"><li class="cleanlist"><g:message code="cv.flags"/>: <g:message code="cv.flags.${params.flags}"/></li></g:if>
                    <g:if test="${params.authority != null}"><li class="cleanlist"><g:message code="cv.authority_shortcode"/>: <g:message code="cv.authority_shortcode.${params.authority}"/></li></g:if>
                  </ul>
                </font>
                </g:if>
              </div> 
            </form> 
          </div> 
        </div>
      </div> 
    </div> 

    <div class="yui3-g main">
      <div class="yui3-u-5-24"> 
          <g:if test="${((search_results != null) && (search_results.facetFields != null))}">
            <g:each in="${search_results.facetFields}" var="fl">

              <%
                request.setAttribute("ops", params.clone())
              %>

              <div class="facet"><g:message code="cv.${fl.name}"/>
                <ul class="cleanlist">
                  <g:each in="${fl.values}" var="flv">
                    <% request.ops."${facetFieldMappings[fl.name] ?: fl.name}" = "${flv.name}" %>
                    <li class="cleanlist"><g:link action="search" controller="search" params="${request.ops}"><g:message code="cv.${fl.name}.${flv.name}"/></g:link> (${flv.count})</li>
                  </g:each>
                </ul>
              </div>
            </g:each>
          </g:if>
          <g:else>
            No facets....
          </g:else>
      </div>
      <div class="yui3-u-7-12 SearchResults">
                <g:if test="${keywords != null}">Search results for "${keywords}"</g:if> ${search_results.results.numFound} records found in ${elapsed} seconds.
          <ul class="cleanlist">
            <g:each status="s" in="${search_results?.results}" var="sr">
        
              <li class="cleanlist">
        
                <g:if test="${provserv.lookupProviderInformation(sr['authority_shortcode'])?.showLogo == true }">
                  <g:if test="${(sr['icon_url_s'] != null ) && ( sr['icon_url_s'].length() > 0 )}">
                    <g:if test="${provserv.lookupProviderInformation(sr['authority_shortcode']) != null }"><img src="${sr['icon_url_s']}" style="float:right"/></g:if>
                    <g:else><img src="${sr['icon_url_s']}" style="float:right" class="opaque50"/></g:else>
                  </g:if>
                </g:if>
                 
                <ul class="cleanlist">
        
                  <li><strong><a href="/ofs/directory/${sr['authority_shortcode']}/${sr['aggregator.internal.id']}">${sr['dc.title']}</a></strong> <g:if test="${( sr['childcare_type_s'] != null )}">${sr['childcare_type_s']}</g:if></li>
        
        
                  <g:if test="${sr['website'] != null}"> <li>Providers own site: 
                    <g:if test="${sr['website'].startsWith('http')}">
                      <a href="${sr['website']}">${sr['website']}</a>.
                    </g:if>
                    <g:else>
                      <a href="http://${sr['website']}">${sr['website']}</a>.
                    </g:else>
                  </g:if>
                  <g:if test="${(sr['ofsted_urn_s'] != null)}"> <li><a href="http://www.ofsted.gov.uk/oxcare_providers/full/(urn)/${sr['ofsted_urn_s']}">Latest ofsted report</a></li> </g:if>
                  <g:if test="${(sr['dc.description'] != null ) && ( sr['dc.description'].length() > 0 )}"><li>${sr['dc.description']}</li></g:if>
        
        
                  <li>Address:
                    <g:if test="${(sr['address.line1'] != null ) && ( sr['address.line1'].length() > 0 )}">${sr['address.line1']}</g:if>
                    <g:if test="${(sr['address.line2'] != null ) && ( sr['address.line2'].length() > 0 )}">, ${sr['address.line2']}</g:if>
                    <g:if test="${(sr['address.line3'] != null ) && ( sr['address.line3'].length() > 0 )}">, ${sr['address.line3']}</g:if>
                    <g:if test="${(sr['address.line4'] != null ) && ( sr['address.line4'].length() > 0 )}">, ${sr['address.line4']}</g:if>
                    <g:if test="${(sr['address.line5'] != null ) && ( sr['address.line5'].length() > 0 )}">, ${sr['address.line5']}</g:if>
                  </li>
                  <g:if test="${sr['distance'] != null}"><li>Approximately ${ ((int)Math.abs( sr['distance'] * 100 ))/100 } - miles from ${place.fqn}</li></g:if>
        
                  <g:if test="${(sr['telephone'] != null ) && ( sr['telephone'].length() > 0 )}">
                    Telephone: <span class="tel"><span class="value"><a href="callto:${sr['telephone']}">${sr['telephone']}</a></span></span><br/>
                  </g:if>
        
                  <g:if test="${(sr['email'] != null ) && ( sr['email'].length() > 0 )}"><li>Email: <a href="mailto:${sr['email']}">${sr['email']}</a></li></g:if>
        
                  <g:if test="${( sr['modified'] != null )}">
        	    <li>Last modified: ${sr['modified'].substring(0,sr['modified'].indexOf("T"))}</li>
                  </g:if>
        
                  <li>
                    <g:if test="${provserv.lookupProviderInformation(sr['authority_shortcode']) != null }">
                      <g:if test="${( sr['feedback_url_s'] != null )}">
                        <g:if test="${(sr['feedback_url_s'].indexOf('@') > 0)}">
                          Via: <a href="mailto:${sr['feedback_url_s']}">${sr['feedback_name_s']}</a>.
                        </g:if>
                        <g:else>
                          Via: 
                          <g:if test="${sr['feedback_url_s'].startsWith('http')}">
                            <a href="${sr['feedback_url_s']}">${sr['feedback_name_s']}</a>.
                          </g:if>
                          <g:else>
                            <a href="http://${sr['feedback_url_s']}">${sr['feedback_name_s']}</a>.
                          </g:else>
                        </g:else>
                      </g:if>
                    </g:if>
                    ${provserv.getDisclaimer(sr['authority_shortcode'])}
                  </li>
        
                </ul>
              </li>
            </g:each>
          </ul>
        
          <div id="pagination"> 
            <g:paginate next="Forward" prev="Back" maxsteps="0" controller="search" action="search" total="${search_results.results.numFound}" params="${params}"> </g:paginate>
          </div> 
      </div>
      <div class="yui3-u-5-24">
        <div style="padding:10px;">
<script type="text/javascript"><!--
google_ad_client = "ca-pub-8968806895227089";
/* OFSRight2 */
google_ad_slot = "2707021068";
google_ad_width = 160;
google_ad_height = 600;
//-->
</script>
<script type="text/javascript"
src="http://pagead2.googlesyndication.com/pagead/show_ads.js">
</script>
        </div>
      </div>

      </div>
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

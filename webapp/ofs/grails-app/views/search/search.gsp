<html>
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
    <!-- Include the main layout from the grails-app/views/layouts dir - Thats where are the styles etc are imported -->
    <meta name="layout" content="main" />
    <title>OFS Search</title>
  </head>
  <body>
    <div id="main">

<div id="wrap">

  <div id="SearchPanel">
    <g:form controller="search" action="search" method="get">
      <table>
        <tr>
          <td>
            Search for: <input type="text" name="q"/> <input type="submit"/>
          </td>
        </tr>
      </table>
    </g:form>
  </div>

<div id="SearchResults">

<g:if test="${noqry != true}">
  There was a query: ${qry}<br/>
  Search found ${search_results.results.numFound} records, showing items starting at ${search_results.results.start}.
  <ul>
    <g:each status="s" in="${search_results.results}" var="sr">
      <li>[${s+search_results.results.start}]
        <ul>
          <li>title: <g:link controller="entry" action="index" id="${sr['aggregator.internal.id']}">${sr['dc.title']}</g:link></li>

          <g:if test="${(sr['dc.description'] != null ) && ( sr['dc.description'].length() > 0 )}"><li>${sr['dc.description']}</li></g:if>
          <g:if test="${(sr['icon_url_s'] != null ) && ( sr['icon_url_s'].length() > 0 )}"><li><img src="${sr['icon_url_s']}"/></li></g:if>

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

          <!--
          <g:each in="${sr}" var="fv">
            <li> ${fv.key} : ${fv.value}</li>
          </g:each>
          -->

        </ul>
      </li>
    </g:each>
  </ul>
</g:if>
<g:else>
  No query, show the search form.
</g:else>
</div><!--SearchResults-->

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
</div>

&nbsp;
    </div><!--Results-->

<div style="clear:both;">&nbsp;</div>

<div id="pagination">
  <g:paginate next="Forward" prev="Back" maxsteps="0" controller="search" action="search" total="${search_results.results.numFound}" params="[q:params.q]"> </g:paginate>
</div>

    </div><!-- main -->
  </body>
</html>

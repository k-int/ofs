<html>
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
    <meta name="layout" content="main" />
    <title>OFS Search</title>
  </head>
  <body>
    <div>

<g:form controller="search" action="search" method="get">
  <table>
    <tr>
      <td>
        Search for: <input type="text" name="q"/> <input type="submit"/>
      </td>
    </tr>
  </table>
</g:form>

<g:if test="${noqry != true}">
  There was a query: ${qry}<br/>
  Search found ${search_results.results.numFound} records, showing items starting at ${search_results.results.start}.
  <ul>
    <g:each status="s" in="${search_results.results}" var="sr">
      <li>[${s+search_results.results.start}]
        <ul>
          <li>title: <g:link controller="entry" action="index" id="${sr['aggregator.internal.id']}">${sr['dc.title']}</g:link></li>

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

          <g:each in="${sr}" var="fv">
            <li> ${fv.key} : ${fv.value}</li>
          </g:each>

        </ul>
      </li>
    </g:each>
  </ul>
</g:if>
<g:else>
  No query, show the search form.
</g:else>

    </div>
  </body>
</html>

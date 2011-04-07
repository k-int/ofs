Search gsp


<g:if test="${noqry != true}">
  There was a query: ${qry}<br/>
  Search found ${search_results.results.numFound} records, showing items starting at ${search_results.results.start}.
  <ul>
    <g:each status="s" in="${search_results.results}" var="sr">
      <li>[${s+search_results.results.start}]
        <ul>
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


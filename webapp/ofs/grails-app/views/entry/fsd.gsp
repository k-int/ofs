<html>
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
    <!-- Include the main layout from the grails-app/views/layouts dir - Thats where are the styles etc are imported -->
    <meta name="layout" content="entry" />

    <meta name="dc.title" content="${entry['dc.title']}" />
    <meta name="title" content="${entry['dc.title']}" />

    <g:if test="${(entry['dc.description'] != null )}">
      <meta name="dc.description" content="${entry['dc.description']}" />
      <meta name="description" content="${entry['dc.description']}" />
    </g:if>

    <g:if test="${(entry['lat'] != null ) && ( entry['lng'] != null ) }">
      <meta name="ICBM" content="${entry['lat']}, ${entry['lng']}" />
      <meta name="geo.position" content="${entry['lat']}, ${entry['lng']}" />
    </g:if>

    <meta name="geo.region" content="${entry['address.line1'] ?: ''} ${entry['address.line2'] ?: ''} ${entry['address.line3'] ?: ''} ${entry['address.line4'] ?: ''} ${entry['address.line5'] ?: ''}" />
    <meta name="geo.placename" content="${entry['address.line1'] ?: ''} ${entry['address.line2'] ?: ''} ${entry['address.line3'] ?: ''} ${entry['address.line4'] ?: ''} ${entry['address.line5'] ?: ''}" />

    <title>${g.message(code: 'ofs.fsd.details.prefix')} ${entry['dc.title']}</title>

    <g:if test="${ ( (entry['lat'] != null ) && ( entry['lng'] != null ) ) }">
      <script type="text/javascript" src="http://maps.google.com/maps/api/js?sensor=false"></script>

      <script type="text/javascript">
      //<![CDATA[

      function map2() {
        var myLatlng = new google.maps.LatLng(${entry['lat']},${entry['lng']});

        var myOptions = {
           zoom: 15,
           center: myLatlng,
           mapTypeId: google.maps.MapTypeId.ROADMAP
        }

        var map = new google.maps.Map(document.getElementById("map"), myOptions);
    
        var marker = new google.maps.Marker({
             position: myLatlng, 
             title:"${entry['dc.title']}"
        });   

        marker.setMap(map);  

      }
      //]]>
      </script>
    </g:if>

  </head>
  <body class="search-results">

    <div>


<g:if test="${(entry['website'] != null ) && ( entry['website'].length() > 0 )}">
  <h1>
    <g:if test="${entry['website'].toLowerCase().startsWith('http')}">
      <a href="${entry['website']}">${entry['dc.title']}</a>
    </g:if>
    <g:else>
      <a href="http://${entry['website']}">${entry['dc.title']}</a>
    </g:else>
  </h1>
</g:if>
<g:else>
<h1>${entry['dc.title']}</h1>
</g:else>

      <g:if test="${entry['dc.subject.orig_s'] != null}">
        <% request.setAttribute("ops", [:]) %>
        <g:if test="${entry['address.postcode'] != null}"><% request.ops.placename="${entry['address.postcode']}" %></g:if>
        <div class="categories">Categories (Links find similar services in the same area): 
          <g:if test="${entry['dc.subject.orig_s'] instanceof java.util.List}">
            <ul>
            <g:each in="${entry['dc.subject.orig_s']}" var="cat" status="i">
              <% request.ops.subject = "${cat}" %>
              <li style="display:inline"><g:link action="search" controller="search" params="${request.ops}"><g:message code="cv.dc.subject.orig_s.${cat}"/></g:link></li>
            </g:each>
            </ul>
          </g:if>
          <g:else>
            <% request.ops.subject = "${entry['dc.subject.orig_s']}" %>
            <g:link action="search" controller="search" params="${request.ops}"><g:message code="cv.dc.subject.orig_s.${entry['dc.subject.orig_s']}"/></g:link>
          </g:else>
        </div>
      </g:if>

    <div id="map" style="width: 250px; height: 250px; float:right"></div>


<g:if test="${(entry['dc.description'] != null ) && ( entry['dc.description'].length() > 0 )}">
<div class="description">${entry['dc.description']}</div>
</g:if>


<g:if test="${(entry['website'] != null ) && ( entry['website'].length() > 0 )}"><div class="website"><strong>Website</strong> <a href="${entry['website']}">${entry['website']}</a></div></g:if>

<g:if test="${(entry['modified'] != null ) && ( entry['modified'].length() > 0 )}"><div><strong>Last Modified</strong> ${entry['modified'].substring(0,entry['modified'].indexOf("T"))}</div></g:if>

<h2>Site Details</h2>

      <g:if test="${(entry['address.line1'] != null ) && ( entry['address.line1'].length() > 0 )}">${entry['address.line1']}<br/></g:if>
      <g:if test="${(entry['address.line2'] != null ) && ( entry['address.line2'].length() > 0 )}">${entry['address.line2']}<br/></g:if>
      <g:if test="${(entry['address.line3'] != null ) && ( entry['address.line3'].length() > 0 )}">${entry['address.line3']}<br/></g:if>
      <g:if test="${(entry['address.line4'] != null ) && ( entry['address.line4'].length() > 0 )}">${entry['address.line4']}<br/></g:if>
      <g:if test="${(entry['address.line5'] != null ) && ( entry['address.line5'].length() > 0 )}">${entry['address.line5']}<br/></g:if>
      <g:if test="${(entry['address.postcode'] != null ) && ( entry['address.postcode'].length() > 0 )}">${entry['address.postcode']}<br/></g:if>
      <g:if test="${(entry['telephone'] != null ) && ( entry['telephone'].length() > 0 )}">Telephone: ${entry['telephone']}<br/></g:if>
      <g:if test="${(entry['email'] != null ) && ( entry['email'].length() > 0 )}">Email: ${entry['email']}<br/></g:if>
      <g:if test="${(entry['fax'] != null ) && ( entry['fax'].length() > 0 )}">Email: ${entry['fax']}<br/></g:if>

<g:if test="${srcdoc != null}">
  <g:if test="${srcdoc.AreaCovered.size() > 0}">
    ${srcdoc.AreaCovered.text()}<br/>
  </g:if>

  <g:if test="${srcdoc.Availability.size() > 0}">
    <h2>Availability</h2>
    <table>
      <tr><th>Period</th><th>Start</th><th>End</th></tr>
      <g:each in="${srcdoc.Availability.Period}" var="p">
        <tr><td>${p.@Day.text()}</td><td>${p.StartTime?.text()}</td><td>${p.EndTime?.text()}</td></tr>
      </g:each>
    </table>
    ${srcdoc.Availability.details?.text()}
  </g:if>

  <g:if test="${srcdoc.SpecialProvisions.size() > 0}">
    <h2>Special Provisions</h2>
    <g:if test="${( ( srcdoc.SpecialProvisions.SpecialNeeds.size() > 0 ) && ( ( srcdoc.SpecialProvisions.SpecialNeeds.@HasProvision=1) || (srcdoc.SpecialProvisions.SpecialNeeds.@HasProvision='true') ) ) }">
      <h3>Special Needs</h3>
      <p>${srcdoc.SpecialProvisions.SpecialNeeds.Experience?.text()}<br/>
         ${srcdoc.SpecialProvisions.SpecialNeeds.Confidence?.text()}<br/>
         ${srcdoc.SpecialProvisions.SpecialNeeds.Details?.text()}
      </p>
    </g:if>
    <g:if test="${( ( srcdoc.SpecialProvisions.WheelchairAccess.size() > 0 ) && ( ( srcdoc.SpecialProvisions.WheelchairAccess.@HasProvision=1) || (srcdoc.SpecialProvisions.WheelchairAccess.@HasProvision='true') ) ) }">
      <h3>Wheelchair Access</h3>
      <p>${srcdoc.SpecialProvisions.WheelchairAccess.text()}</p>
    </g:if>
    <g:if test="${( ( srcdoc.SpecialProvisions.CulturalProvision.size() > 0 ) && ( ( srcdoc.SpecialProvisions.CulturalProvision.@HasProvision=1) || (srcdoc.SpecialProvisions.CulturalProvision.@HasProvision='true') ) ) }">
      <h3>Cultural Provision</h3>
      <p>${srcdoc.SpecialProvisions.CulturalProvision.text()}</p>
    </g:if>
    <g:if test="${( ( srcdoc.SpecialProvisions.SpecialDiet.size() > 0 ) && ( ( srcdoc.SpecialProvisions.SpecialDiet.@HasProvision=1) || (srcdoc.SpecialProvisions.SpecialDiet.@HasProvision='true') ) ) }">
      <h3>Special Diet</h3>
      <p>${srcdoc.SpecialProvisions.SpecialDiet.text()}</p>
    </g:if>
  </g:if>

  <g:if test="${srcdoc.CostDetails.size() > 0}">
    <h2>Costs</h2>
    ${srcdoc.SpecialProvisions.Costs.text()}
    ${srcdoc.SpecialProvisions.OtherCosts.text()}
  </g:if>
</g:if>

<g:if test="${entry['flags'] != null}">
  <ul>
    <g:outputSolrProperty prop="${entry['flags']}" />
  </ul>
</g:if>

<g:if test="${entry['extra_index_words_s'] != null}">
Keywords
<ul>
  <g:outputSolrProperty prop="${entry['extra_index_words_s']}" />
</ul>
</g:if>


<g:if test="${(entry['other_info_s'] != null ) && ( entry['other_info_s'].length() > 0 )}"><li>Other Information: ${entry['other_info_s']}</li></g:if>

</div>
    <script language="JavaScript">
      map2();
    </script>

</body>
</html>

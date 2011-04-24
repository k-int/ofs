<html>
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
    <!-- Include the main layout from the grails-app/views/layouts dir - Thats where are the styles etc are imported -->
    <meta name="layout" content="main" />

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
  <body>
    <div>

<h1><li><strong>${entry['dc.title']}</strong></li></h1>

<g:if test="${entry['dc.subject.orig_s'] != null}">
  <div>In Categories<br/>
  <ul>
    <g:each in=
    <g:each in="${entry['dc.subject.orig_s']}" var="sub">
      <li><g:message code="cv.dc.subject.orig_s.${sub}"/></li>
    </g:each>
  </ul>
</div>
</g:if>

   <!-- Float this right? -->
    <div id="map" style="width: 250px; height: 250px"></div>


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
      <g:if test="${(entry['telephone'] != null ) && ( entry['telephone'].length() > 0 )}"><li>Telephone: ${entry['telephone']}</li></g:if>
      <g:if test="${(entry['email'] != null ) && ( entry['email'].length() > 0 )}"><li>Email: ${entry['email']}</li></g:if>
      <g:if test="${(entry['fax'] != null ) && ( entry['fax'].length() > 0 )}"><li>Email: ${entry['fax']}</li></g:if>

-- Down to here --


<ul>
  <li>Basic Details
    <ul>



      <g:if test="${( entry['ispp.age_min'] != null ) && ( entry['ispp.age_max'] != null )}">
         <li>Age Range: from ${entry['ispp.age_min']} to ${entry['ispp.age_max']} years
      </g:if>

      <g:if test="${(entry['childcare_type_s'] != null ) && ( entry['childcare_type_s'].length() > 0 )}"><li>Childcare Type: ${entry['childcare_type_s']}</li></g:if>

      <li>Flags
        <g:if test="${entry['flags'] != null}">
          <ul>
            <g:outputSolrProperty prop="${entry['flags']}" />
          </ul>
        </g:if>
      </li>
      <li>Age Groups</li>
      <li>Keywords
        <g:if test="${entry['extra_index_words_s'] != null}">
          <ul>
            <g:outputSolrProperty prop="${entry['extra_index_words_s']}" />
          </ul>
        </g:if>
      </li>

     <g:if test="${(entry['other_info_s'] != null ) && ( entry['other_info_s'].length() > 0 )}"><li>Other Information: ${entry['other_info_s']}</li></g:if>

    </ul>
  </li>

  <li>Places
    <ul>
      <li>Place Availability</li>
    </ul>
  </li>

  <li>Opening Times
    <ul>
      <li>Availability</li>
      <li>Opening Periods</li>
    </ul>
  </li>

  <li>Special Provision
    <ul>
    </ul>
  </li>

  <li>Costs
    <ul>
    </ul>
  </li>

  <li>Pickup
    <ul>
    </ul>
  </li>


  <li>Ofsted Registration
    <ul>
    </ul>
  </li>

  <li>Other Details
    <ul>
    </ul>
  </li>

  <li>Map
    <ul>
<g:if test="${(entry['lat'] != null )}"><li>Lat:${entry['lat']}</li></g:if>
<g:if test="${(entry['lng'] != null )}"><li>Lng:${entry['lng']}</li></g:if>
    </ul>
  </li>

</ul>

</div>
    <script language="JavaScript">
      map2();
    </script>

</body>
</html>

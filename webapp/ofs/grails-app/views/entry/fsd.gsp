<html>
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
    <!-- Include the main layout from the grails-app/views/layouts dir - Thats where are the styles etc are imported -->
    <meta name="layout" content="main" />
    <title>OFS Search - FSD Entry</title>
  </head>
  <body>
    <div>

FSD
<ul>
  <li>Basic Details
    <ul>
      <li>Title: ${entry['dc.title']}</li>
      <g:if test="${(entry['dc.description'] != null ) && ( entry['dc.description'].length() > 0 )}"><li>Description: ${entry['dc.description']}</li></g:if>

      <li>Categories
        <g:if test="${entry['dc.subject.orig_s'] != null}">
          <ul>
            <!--outputSolrProperty is a custom taglib defined in grails-app/taglibs/ofs/OfsTagLib.groovy. It outputs li elements for scalar or list values -->
            <g:outputSolrProperty prop="${entry['dc.subject.orig_s']}" />
          </ul>
        </g:if>
      </li>

      <g:if test="${(entry['dc.description'] != null ) && ( entry['dc.description'].length() > 0 )}"><li>Description: ${entry['dc.description']}</li></g:if>
      <g:if test="${(entry['modified'] != null ) && ( entry['modified'].length() > 0 )}"><li>Last Modified: ${entry['dc.description']}</li></g:if>

      <li>Contact Person</li>
      <li>Address:<br/>
        <g:if test="${(entry['address.line1'] != null ) && ( entry['address.line1'].length() > 0 )}">${entry['address.line1']}<br/></g:if>
        <g:if test="${(entry['address.line2'] != null ) && ( entry['address.line2'].length() > 0 )}">${entry['address.line2']}<br/></g:if>
        <g:if test="${(entry['address.line3'] != null ) && ( entry['address.line3'].length() > 0 )}">${entry['address.line3']}<br/></g:if>
        <g:if test="${(entry['address.line4'] != null ) && ( entry['address.line4'].length() > 0 )}">${entry['address.line4']}<br/></g:if>
        <g:if test="${(entry['address.line5'] != null ) && ( entry['address.line5'].length() > 0 )}">${entry['address.line5']}<br/></g:if>
      </li>
      <g:if test="${(entry['telephone'] != null ) && ( entry['telephone'].length() > 0 )}"><li>Telephone: ${entry['telephone']}</li></g:if>
      <g:if test="${(entry['email'] != null ) && ( entry['email'].length() > 0 )}"><li>Email: ${entry['email']}</li></g:if>
      <g:if test="${(entry['fax'] != null ) && ( entry['fax'].length() > 0 )}"><li>Email: ${entry['fax']}</li></g:if>

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
</body>
</html>

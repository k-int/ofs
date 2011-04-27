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

    <title>${g.message(code: 'ofs.details.prefix')} ${entry['dc.title']}</title>

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
             map: map, 
             title:"${entry['dc.title']}"
        });   
        marker.setMap(map);  
      }
      //]]>
      </script>
    </g:if>

  </head>
  <body class="search-results">
    <h1>
      <g:if test="${(entry['website'] != null ) && ( entry['website'].length() > 0 )}">
        <g:if test="${entry['website'].toLowerCase().startsWith('http')}">
          <a href="${entry['website']}">${entry['dc.title']}</a>
        </g:if>
        <g:else>
          <a href="http://${entry['website']}">${entry['dc.title']}</a>
        </g:else>
      </g:if>
      <g:else>
        ${entry['dc.title']}
      </g:else>
      <g:if test="${( entry['childcare_type_s'] != null )}"> - ${entry['childcare_type_s']}</g:if>
    </h1>

    <div>
      <g:if test="${(entry['dc.description'] != null ) && ( entry['dc.description'].length() > 0 )}"><div class="description">Description: ${entry['dc.description']}</div></g:if>

      <g:if test="${entry['dc.subject.orig_s'] != null}">
        <div class="categories">Categories: 
          <g:if test="${entry['dc.subject.orig_s'] instanceof java.util.List}">
            <g:each in="${entry['dc.subject.orig_s']}" var="cat" status="i"><g:if test="${i > 0}">,&nbsp;</g:if><a href="/ofs/?subject=${cat}"><g:message code="cv.dc.subject.orig_s.${cat}"/></g:each></a>
          </g:if>
          <g:else>
            <g:message code="cv.dc.subject.orig_s.${entry['dc.subject.orig_s']}"/>
          </g:else>
        </div>
      </g:if>

    <div id="map" style="width: 250px; height: 250px; float:right"></div>

  <h2>Basic Details</h2>

      <g:if test="${(entry['modified'] != null ) && ( entry['modified'].length() > 0 )}">Last Modified: ${entry['dc.description']}<br/></g:if>

      <g:if test="${(entry['ofsted_urn_s'] != null)}">
        <a href="http://www.ofsted.gov.uk/oxcare_providers/full/(urn)/${entry['ofsted_urn_s']}">Latest ofsted report</a><br/>
      </g:if>

      <h2>Contact Details</h2>
      <h3>Address</h3>
      <g:if test="${(entry['address.line1'] != null ) && ( entry['address.line1'].length() > 0 )}">${entry['address.line1']}<br/></g:if>
      <g:if test="${(entry['address.line2'] != null ) && ( entry['address.line2'].length() > 0 )}">${entry['address.line2']}<br/></g:if>
      <g:if test="${(entry['address.line3'] != null ) && ( entry['address.line3'].length() > 0 )}">${entry['address.line3']}<br/></g:if>
      <g:if test="${(entry['address.line4'] != null ) && ( entry['address.line4'].length() > 0 )}">${entry['address.line4']}<br/></g:if>
      <g:if test="${(entry['address.line5'] != null ) && ( entry['address.line5'].length() > 0 )}">${entry['address.line5']}<br/></g:if>
      <g:if test="${(entry['address.postcode'] != null ) && ( entry['address.postcode'].length() > 0 )}">${entry['address.postcode']}<br/></g:if>

      <g:if test="${(entry['telephone'] != null ) && ( entry['telephone'].length() > 0 )}">Telephone: ${entry['telephone']}</g:if>
      <g:if test="${(entry['email'] != null ) && ( entry['email'].length() > 0 )}">Email: ${entry['email']}</g:if>
      <g:if test="${(entry['fax'] != null ) && ( entry['fax'].length() > 0 )}">Email: ${entry['fax']}</g:if>

      <h2>General</h2>
      <g:if test="${( entry['ispp.age_min'] != null ) && ( entry['ispp.age_max'] != null )}">
         Age Range: from ${entry['ispp.age_min']} to ${entry['ispp.age_max']} years</br>
      </g:if>

      <g:if test="${(entry['childcare_type_s'] != null ) && ( entry['childcare_type_s'].length() > 0 )}">Childcare Type: ${entry['childcare_type_s']}</g:if>

      <h2>Ofsted Registration</h2>
      <ul>
        <g:if test="${srcdoc != null}">
          <g:if test="${srcdoc.ProviderDetails != null}">
            <g:if test="${srcdoc.ProviderDetails.ChildcareAges != null}">
              <h3>Registered Places</h3>
              <div id="Registered Places">
                <g:each in="${srcdoc.ProviderDetails.ChildcareAges.ChildcareAge}" var="cc">
                  ${cc.AgeFrom.text()} to ${cc.AgeTo.text()} years : ${cc.ChildNumberLimit.text()} places <br/>
                </g:each>
              </div>
            </g:if>
              <g:if test="${srcdoc.ProviderDetails.FutureVacancyDetails.size() > 0}">
                <h3>Available places</h3>
                <g:if test="${(srcdoc.ProviderDetails.FutureVacancyDetails.@ContactForVacancies == 1 ) || ( srcdoc.ProviderDetails.FutureVacancyDetails.@ContactForVacancies == 'true' ) }">
                  Please contact the provider for details of current vacancies
                </g:if>
              </g:if>
            <h3>Registration Date</h3>
            ${srcdoc.ProviderDetails.RegistrationDetails.RegistrationDate.text()}

            <g:if test="${(srcdoc.ProviderDetails.RegistrationDetails.RegistrationConditions.size() > 0 ) && ( srcdoc.ProviderDetails.RegistrationDetails.RegistrationConditions.RegistrationCondition.size() > 0 ) }">
            <h3>Registration Conditions</h3>
              <ul>
                <g:each in="${srcdoc.ProviderDetails.RegistrationDetails.RegistrationConditions.RegistrationCondition}" var="rc">
                  <li>${rc.text()}</li>
                </g:each>
              </ul>
            </g:if>

            <g:if test="${srcdoc.ProviderDetails.RegistrationDetails.RegistrationStatus.size() > 0}">
              <h3>Registration Status</h3> ${srcdoc.ProviderDetails.RegistrationDetails.RegistrationStatus.RegistrationStatus.text()} 
              (Started: ${srcdoc.ProviderDetails.RegistrationDetails.RegistrationStatus.RegistrationStatusStartDate})
            </g:if>

            <g:if test="${srcdoc.ProviderDetails.RegistrationDetails.LastInspection != null}">
              <g:if test="${srcdoc.ProviderDetails.RegistrationDetails.LastInspection.InspectionDate.size() > 0}">
                <h3>Last Inspection</h3>${srcdoc.ProviderDetails.RegistrationDetails.LastInspection.InspectionDate.text()}
              </g:if>
              <g:if test="${srcdoc.ProviderDetails.RegistrationDetails.LastInspection.InspectionType.size() > 0}">
                <h3>Inspection Type</h3>${srcdoc.ProviderDetails.RegistrationDetails.LastInspection.InspectionType.text()}
              </g:if>
              <g:if test="${srcdoc.ProviderDetails.RegistrationDetails.LastInspection.InspectionOverallJudgementDescription.size() > 0}">
                <h3>Overall Judgement</h3>${srcdoc.ProviderDetails.RegistrationDetails.LastInspection.InspectionOverallJudgementDescription.text()}
              </g:if>
            </g:if>


          </g:if>
        </g:if>
      </ul>

      <h2>Additional Information</h2>
      <g:if test="${entry['flags'] != null}">
        <div class="categories">Features and Facilities: 
          <g:if test="${entry['flags'] instanceof java.util.List}">
            <g:each in="${entry['flags']}" var="flag" status="i"><g:if test="${i > 0}">,&nbsp;</g:if><a href="/ofs/?flags=${flag}"><g:message code="cv.flags.${flag}"/></a></g:each>
          </g:if>
          <g:else>NA
            <g:message code="cv.flags.${entry['flags']}"/>
          </g:else>
        </div>
      </g:if>

      <g:if test="${entry['extra_index_words_s'] != null}">
      <h3>Keywords</h3>
        <ul>
          <g:outputSolrProperty prop="${entry['extra_index_words_s']}" />
        </ul>
      </g:if>

      <g:if test="${(entry['other_info_s'] != null ) && ( entry['other_info_s'].length() > 0 )}">Other Information: ${entry['other_info_s']}<br/></g:if>

</div>
    <script language="JavaScript">
      map2();
    </script>
</body>
</html>

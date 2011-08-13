<html>
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
    <!-- Include the main layout from the grails-app/views/layouts dir - Thats where are the styles etc are imported -->
    <meta name="layout" content="entry" />
    <meta property="dc.title" name="dc.title" content="${entry['dc.title']}" />
    <meta property="title" name="title" content="${entry['dc.title']}" />

    <g:if test="${(entry['dc.description'] != null )}">
      <meta property="dc.description" name="dc.description" content="${entry['dc.description']} " />
      <meta property="description" name="description" content="${entry['dc.title']} ${entry['feedback_name_s']}, #${params.id} ${entry['dc.description']}" />
      <meta property="og:description" content="${entry['dc.description']}" />
    </g:if>

    <g:if test="${(entry['lat'] != null ) && ( entry['lng'] != null ) }">
      <meta property="ICBM" name="ICBM" content="${entry['lat']}, ${entry['lng']}" />
      <meta property="geo.position" name="geo.position" content="${entry['lat']}, ${entry['lng']}" />
      <meta property="og:latitude" content="${entry['lat']}" />
      <meta property="og:longitude" content="${entry['lng']}" />
    </g:if>

    <meta property="geo.region" name="geo.region" content="${entry['address.line1'] ?: ''} ${entry['address.line2'] ?: ''} ${entry['address.line3'] ?: ''} ${entry['address.line4'] ?: ''} ${entry['address.line5'] ?: ''}" />
    <meta property="geo.placename" name="geo.placename" content="${entry['address.line1'] ?: ''} ${entry['address.line2'] ?: ''} ${entry['address.line3'] ?: ''} ${entry['address.line4'] ?: ''} ${entry['address.line5'] ?: ''}" />

    <!-- OGP Properties -->
    <meta property="og:title" content="${entry['dc.title']}" />
    <meta property="og:type" content="activity" />
    <meta property="og:url" content="${grailsApplication.config.ofs.frontend}/ofs/directory/${entry['authority_shortcode']}/${entry['aggregator.internal.id']}" />
    <g:if test="${entry['icon_url_s']!=null}"><meta property="og:image" content="${entry['icon_url_s']}" /></g:if>
    <meta property="og:site_name" content="Open Family Services" />

    <title>${entry['dc.title']} (via ${entry['feedback_name_s']}, #${params.id})</title>

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
    
        // var marker = new google.maps.Marker({
        //      position: myLatlng, 
        //      map: map, 
        //      title:"${entry['dc.title']}"
        // });   
        // marker.setMap(map);  
      }
      //]]>
      </script>
    </g:if>

  </head>
  <body class="search-results">
    <h1>
      <g:if test="${(entry['website'] != null ) && ( entry['website'].length() > 0 )}">
        <g:if test="${entry['website'].toLowerCase().startsWith('http')}">
          <a href="${entry['website']}">${entry['dc.title']}</a></g:if>
        <g:else>
          <a href="http://${entry['website']}">${entry['dc.title']}</a></g:else>
      </g:if><g:else>
        ${entry['dc.title']}</g:else>
      <g:if test="${( entry['childcare_type_s'] != null )}"> - ${entry['childcare_type_s']}</g:if>
    </h1>

    <div>
      <g:if test="${(entry['dc.description'] != null ) && ( entry['dc.description'].length() > 0 )}"><div class="description">Description: ${entry['dc.description']}</div></g:if>

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

    <div id="rightpanel" style="float:right; width:250px;"> 
      <div id="map" style="width: 250px; height: 250px;"></div>
      <div style="text-align: center; margin-top:15px; width: 250px;">Please contact the provider for exact location</div>
    </div>

  <h2>Basic Details</h2>

      <g:if test="${(entry['modified'] != null ) && ( entry['modified'].length() > 0 )}">Last Modified: ${entry['modified']}<br/></g:if>

      <g:if test="${( entry['feedback_url_s'] != null )}">
        <g:if test="${(entry['feedback_url_s'].indexOf('@') > 0)}">
          Via: <a href="mailto:${entry['feedback_url_s']}">${entry['feedback_name_s']}</a>.
        </g:if>
        <g:else>
          Via: <a href="http://${entry['feedback_url_s']}">${entry['feedback_name_s']}</a>.
        </g:else>
      </g:if>

      <g:if test="${provserv.lookupProviderInformation(entry['authority_shortcode']) != null }">
        ${entry['feedback_name_s']} is an active contributor to Open Family Services.
      </g:if>
      <g:else>
        ${entry['feedback_name_s']} Does not actively update records in this service, and takes no responsibility for it's accuracy. The information may be wrong or out of date.
      </g:else>
      </br>

      <h2>Contact Details</h2>
      <h3>Address</h3>
      <g:if test="${(entry['address.line1'] != null ) && ( entry['address.line1'].length() > 0 )}">${entry['address.line1']}<br/></g:if>
      <g:if test="${(entry['address.line2'] != null ) && ( entry['address.line2'].length() > 0 )}">${entry['address.line2']}<br/></g:if>
      <g:if test="${(entry['address.line3'] != null ) && ( entry['address.line3'].length() > 0 )}">${entry['address.line3']}<br/></g:if>
      <g:if test="${(entry['address.line4'] != null ) && ( entry['address.line4'].length() > 0 )}">${entry['address.line4']}<br/></g:if>
      <g:if test="${(entry['address.line5'] != null ) && ( entry['address.line5'].length() > 0 )}">${entry['address.line5']}<br/></g:if>
      <g:if test="${(entry['address.postcode'] != null ) && ( entry['address.postcode'].length() > 0 )}">${entry['address.postcode']}<br/></g:if>
      <g:if test="${(entry['telephone'] != null ) && ( entry['telephone'].length() > 0 )}">Telephone: ${entry['telephone']}<br/></g:if>
      <g:if test="${(entry['email'] != null ) && ( entry['email'].length() > 0 )}">Email: <a href="mailto:${entry['email']}">${entry['email']}</a><br/></g:if>
      <g:if test="${(entry['fax'] != null ) && ( entry['fax'].length() > 0 )}">Email: ${entry['fax']}<br/></g:if>

      <h2>General</h2>

      <g:if test="${(entry['childcare_type_s'] != null ) && ( entry['childcare_type_s'].length() > 0 )}">Childcare Type: ${entry['childcare_type_s']}</g:if>

      <h2>Ofsted Registration</h2>

        <g:if test="${(entry['ofsted_urn_s'] != null)}">
          <h3>Official Ofsted Report</h3>
          <a href="http://www.ofsted.gov.uk/oxcare_providers/full/(urn)/${entry['ofsted_urn_s']}">Latest ofsted report</a><br/>
        </g:if>

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

            <g:if test="${((srcdoc.ProviderDetails.FutureVacancyDetails.size() > 0) || (srcdoc.ProviderDetails.ImmediateVacancies.size() > 0) )}">
              <h3>Available places</h3>
              <g:if test="${srcdoc.ProviderDetails.ImmediateVacancies.size() > 0}">
                This provider has immediate vacancies.
              </g:if>
              <g:if test="${srcdoc.ProviderDetails.FutureVacancyDetails.size() > 0}">
                <g:if test="${(srcdoc.ProviderDetails.FutureVacancyDetails.@ContactForVacancies == 1 ) || ( srcdoc.ProviderDetails.FutureVacancyDetails.@ContactForVacancies == 'true' ) }">
                  Please contact the provider for details of future vacancies.
                </g:if>
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

      
      <g:if test="${srcdoc.ProviderDetails.CostDetails.size() > 0}">
        <h2>Costs</h2>
        ${srcdoc.ProviderDetails.CostDetails.Costs.text()}
  
        <g:if test="${srcdoc.ProviderDetails.CostDetails.OtherCosts.size() > 0}">
          ${srcdoc.ProviderDetails.CostDetails.OtherCosts.text()}
        </g:if>
      </g:if>

      <g:if test="${srcdoc.ProviderDetails.ChildcareTimes.size() > 0}">
        <h2>Availability</h2>
        ${srcdoc.ProviderDetails.ChildcareTimes.Availability.text()}
      </g:if>

      <g:if test="${srcdoc.ProviderDetails.Pickups.size() > 0}">
        <h2>School Pickups</h2>
         <g:if test="${srcdoc.ProviderDetails.Pickups.SchoolList.size() > 0}">
           School List: ${srcdoc.ProviderDetails.Pickups.SchoolList.text()}<br/>
         </g:if>
         <g:if test="${srcdoc.ProviderDetails.Pickups.Details.size() > 0}">
           Details: ${srcdoc.ProviderDetails.Pickups.Details.text()}<br/>
         </g:if>
      </g:if>

      <g:if test="${srcdoc.ProviderDetails.SpecialProvisions.size() > 0}">
        <h2>Special Provision</h2>

        <g:if test="${(srcdoc.ProviderDetails.SpecialProvisions.SpecialNeeds.size() > 0) && ( ( srcdoc.ProviderDetails.SpecialProvisions.SpecialNeeds.@HasProvision=1) || ( srcdoc.ProviderDetails.SpecialProvisions.SpecialNeeds.@HasProvision='true') ) }">
          <p> <h3>Special Needs</h3>
            ${srcdoc.ProviderDetails.SpecialProvisions.SpecialNeeds.Experience?.text()}<br/>
            ${srcdoc.ProviderDetails.SpecialProvisions.SpecialNeeds.Details?.text()}
          </p>
        </g:if>

        <g:if test="${(srcdoc.ProviderDetails.SpecialProvisions.SpecialDiet.size() > 0) && ( ( srcdoc.ProviderDetails.SpecialProvisions.SpecialDiet.@HasProvision=1) || (srcdoc.ProviderDetails.SpecialProvisions.SpecialDiet.@HasProvision=true) )  }">
          <p> Special provision for diet available at this provider <br/>
            ${srcdoc.ProviderDetails.SpecialProvisions.SpecialDiet.text()}<br/>
          </p>
        </g:if>

        <g:if test="${(srcdoc.ProviderDetails.SpecialProvisions.CulturalProvision.size() > 0) && ( (srcdoc.ProviderDetails.SpecialProvisions.CulturalProvision.@HasProvision=1) || ( srcdoc.ProviderDetails.SpecialProvisions.CulturalProvision.@HasProvision=true) ) }">
          <p> Cultural Provision is Available at this provider<br/>
            ${srcdoc.ProviderDetails.SpecialProvisions.CulturalProvision.text()}<br/>
          </p>
        </g:if>

        <g:if test="${(srcdoc.ProviderDetails.SpecialProvisions.WheelchairAccess.size() > 0) && ( (srcdoc.ProviderDetails.SpecialProvisions.WheelchairAccess.@HasProvision=1) || ( srcdoc.ProviderDetails.SpecialProvisions.WheelchairAccess.@HasProvision=true) ) }">
          <p> Wheelchair Access is Available at this provider<br/>
            ${srcdoc.ProviderDetails.SpecialProvisions.WheelchairAccess.text()}<br/>
          </p>
        </g:if>

      </g:if>

     

      <h2>Additional Information</h2>

      <g:if test="${srcdoc.ProviderDetails.Facilities.size() > 0}">
        <p>Providers description of available facilities: ${srcdoc.ProviderDetails.Facilities.text()}</p>
      </g:if>

      <g:if test="${entry['flags'] != null}">
        <div class="categories">Other Features and Facilities: 
          <g:if test="${entry['flags'] instanceof java.util.List}">
            <g:each in="${entry['flags']}" var="flag" status="i"><g:if test="${i > 0}">,&nbsp;</g:if><g:message code="cv.flags.${flag}"/></g:each>
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

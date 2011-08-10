package com.k_int.ofsd

import org.springframework.context.i18n.LocaleContextHolder as LCH

class ProviderInformationService {

    static transactional = true
    def messageSource

    def members = [
      'Birmingham_City_Council' :                     ["subscriptionType":"basic", "name":"Birmingham City Council", "showLogo":true],
      'Bolton_Council' :                              ["subscriptionType":"basic", "name":"Bolton Council", "showLogo":true],
      'Borough_of_Poole' :                            ["subscriptionType":"basic", "name":"Borough of Poole", "showLogo":true],
      'Bradford_Metropolitan_District_Council' :      ["subscriptionType":"basic", "name":"Bradford Metropolitan District Council", "showLogo":true],
      'Buckinghamshire_County_Council' :              ["subscriptionType":"basic", "name":"Buckinghamshire County Council", "showLogo":true],
      'City_of_York' :                                ["subscriptionType":"basic", "name":"York", "showLogo":true],
      'Devon_County_Council' :                        ["subscriptionType":"basic", "name":"Devon County Council", "showLogo":true],
      'Dorset__County_Council' :                      ["subscriptionType":"basic", "name":"Dorset", "showLogo":true],
      'Dorset_County_Council' :                       ["subscriptionType":"basic", "name":"Dorset", "showLogo":true],  // This is how it is on test :(
      'East_Sussex_County_Council' :                  ["subscriptionType":"basic", "name":"East Sussex County Council", "showLogo":true],  // This is how it is on test :(
      'Halton_Borough_Council' :                      ["subscriptionType":"basic", "name":"Halton Borough Council", "showLogo":true],
      'Kent_County_Council' :                         ["subscriptionType":"basic", "name":"Kent", "showLogo":true],
      'London_Borough_of_Brent' :                     ["subscriptionType":"basic", "name":"London Borough of Brent", "showLogo":true],
      'London_Borough_of_Greenwich_Council' :         ["subscriptionType":"basic", "name":"London Borough of Greenwich Council", "showLogo":true],
      'London_Borough_of_Hackney_Council' :           ["subscriptionType":"basic", "name":"London Borough of Hackney Council", "showLogo":true],
      'London_Borough_of_Lewisham' :                  ["subscriptionType":"basic", "name":"London Borough of Lewisham", "showLogo":true],
      'London_Borough_of_Wandsworth_Council' :        ["subscriptionType":"basic", "name":"Wandsworth", "showLogo":true],
      'Lincolnshire_County_Council' :                 ["subscriptionType":"none",  "name":"Lincolnshire County Council", "showLogo":true],
      'Luton_Borough_Council' :                       ["subscriptionType":"basic", "name":"Luton", "showLogo":true],
      'Manchester_City_Council' :                     ["subscriptionType":"basic", "name":"Manchester City Council", "showLogo":true],
      'Middlesbrough_Council' :                       ["subscriptionType":"none",  "name":"Middlesborough", "showLogo":false,
                                                       "sourceDisclaimer":"The information displayed for Middlesborough has been drawn from a database which was closed on 31 March, and consequently some records may be out of date. For up to date information please contact (Middlesborough FIS)."],
      'North_Lincolnshire_Council' :                  ["subscriptionType":"basic", "name":"North Lincolnshire Council", "showLogo":true],
      'North_Somerset_Council' :                      ["subscriptionType":"basic", "name":"North Somerset", "showLogo":true],
      'North_Yorkshire_County_Council' :              ["subscriptionType":"basic", "name":"North Yorkshire County Council", "showLogo":true],
      'Oxfordshire_County_Council' :                  ["subscriptionType":"basic", "name":"Oxfordshire", "showLogo":true],
      'Rochdale_Metropolitan_Borough_Council' :       ["subscriptionType":"basic", "name":"Rochdale", "showLogo":true],
      'Sheffield_City_Council' :                      ["subscriptionType":"basic", "name":"Sheffield", "showLogo":true],
      'Southend-on-Sea_Borough_Council' :             ["subscriptionType":"basic", "name":"Southend", "showLogo":true],
      'Somerset__County_Council' :                    ["subscriptionType":"basic", "name":"Somerset FID", "showLogo":true],
      'Staffordshire_County_Councili' :               ["subscriptionType":"basic", "name":"Staffordshire County Council", "showLogo":true],
      'Suffolk_County_Council' :                      ["subscriptionType":"basic", "name":"Suffolk", "showLogo":true],
      'Surrey_County_Council' :                       ["subscriptionType":"basic", "name":"Surrey", "showLogo":true],
      'The_Royal_Borough_of_Windsor_and_Maidenhead' : ["subscriptionType":"basic", "name":"The Royal Borough of Windsor and Maidenhead", "showLogo":true],
      'Wirral_Metropolitan_Borough_Council' :         ["subscriptionType":"basic", "name":"Wirral Metropolitan Borough Council", "showLogo":true]
    ]

    def lookupProviderInformation(provider_shortcode) {
      members[provider_shortcode]
    }

    def getDisclaimer(provider_shortcode) {

      def auth_name = messageSource.getMessage("cv.authority_shortcode.${provider_shortcode}",null,null,Locale.ENGLISH)
      def result = "(Derived from information prepared by ${auth_name})"

      if ( provider_shortcode != null ) {
        if ( members[provider_shortcode]?.sourceDisclaimer != null )
          result = "(${members[provider_shortcode]?.sourceDisclaimer})"
        else if ( ( members[provider_shortcode]?.subscriptionType == 'basic' ) || ( members[provider_shortcode]?.subscriptionType == 'subscriber' ) )
          result = "(an active contributor to Open Family Services)"
      }

      result
    }
}

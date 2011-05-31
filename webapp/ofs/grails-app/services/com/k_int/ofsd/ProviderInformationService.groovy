package com.k_int.ofsd

class ProviderInformationService {

    static transactional = true

    def members = [
      'Dorset__County_Council' :                ["subscriptionType":"basic", 
                                                 "name":"Dorset", 
                                                 "showLogo":true],
      'Dorset_County_Council' :                 ["subscriptionType":"basic", 
                                                 "name":"Dorset", 
                                                 "showLogo":true],  // This is how it is on test :(
      'Kent_County_Council' :                   ["subscriptionType":"basic", 
                                                 "name":"Kent", 
                                                 "showLogo":true],
      'Luton_Borough_Council' :                 ["subscriptionType":"basic", 
                                                 "name":"Luton", 
                                                 "showLogo":true],
      'North_Somerset_Council' :                ["subscriptionType":"basic", 
                                                 "name":"North Somerset", 
                                                 "showLogo":true],
      'Oxfordshire_County_Council' :            ["subscriptionType":"basic", 
                                                 "name":"Oxfordshire", 
                                                 "showLogo":true],
      'Rochdale_Metropolitan_Borough_Council' : ["subscriptionType":"basic", 
                                                 "name":"Rochdale", 
                                                 "showLogo":true],
      'Sheffield_City_Council' :                ["subscriptionType":"basic", 
                                                 "name":"Sheffield", 
                                                 "showLogo":true],
      'Southend-on-Sea_Borough_Council' :       ["subscriptionType":"basic", 
                                                 "name":"Southend", 
                                                 "showLogo":true],
      'Suffolk_County_Council' :                ["subscriptionType":"basic", 
                                                 "name":"Suffolk", 
                                                 "showLogo":true],
      'Surrey_County_Council' :                 ["subscriptionType":"basic", 
                                                 "name":"Surrey", 
                                                 "showLogo":true],
      'City_of_York' :                          ["subscriptionType":"basic", 
                                                 "name":"York", 
                                                 "showLogo":true],
      'London_Borough_of_Wandsworth_Council' :  ["subscriptionType":"basic", 
                                                 "name":"Wandsworth", 
                                                 "showLogo":true],
      'Middlesbrough_Council' :                 ["subscriptionType":"none", 
                                                 "name":"Middlesborough", 
                                                 "showLogo":false,
                                                 "sourceDisclaimer":"The information displayed for Middlesborough has been drawn from a database which was closed on 31 March, and consequently some records may be out of date. For up to date information please contact (Middlesborough FIS)."]
    ]

    def lookupProviderInformation(provider_shortcode) {
      members[provider_shortcode]
    }

    def getDisclaimer(provider_shortcode) {

      def result = "(Derived from information prepared by ${provider_shortcode})"

      if ( provider_shortcode != null ) {
        if ( members[provider_shortcode]?.sourceDisclaimer != null )
          result = "(${members[provider_shortcode]?.sourceDisclaimer})"
        else if ( ( members[provider_shortcode]?.subscriptionType == 'basic' ) || ( members[provider_shortcode]?.subscriptionType == 'subscriber' ) )
          result = "(an active contributor to Open Family Services)"
      }

      result
    }
}

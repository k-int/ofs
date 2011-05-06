package com.k_int.ofsd

class ProviderInformationService {

    static transactional = true

    def members = [
      'Dorset__County_Council' :                ["name":"Dorset", "showLogo":true],
      'Dorset_County_Council' :                 ["name":"Dorset", "showLogo":true],  // This is how it is on test :(
      'Kent_County_Council' :                   ["name":"Kent", "showLogo":true],
      'Luton_Borough_Council' :                 ["name":"Luton", "showLogo":true],
      'North_Somerset_Council' :                ["name":"North Somerset", "showLogo":true],
      'Oxfordshire_County_Council' :            ["name":"Oxfordshire", "showLogo":true],
      'Rochdale_Metropolitan_Borough_Council' : ["name":"Rochdale", "showLogo":true],
      'Sheffield_City_Council' :                ["name":"Sheffield", "showLogo":true],
      'Southend-on-Sea_Borough_Council' :       ["name":"Southend", "showLogo":true],
      'Suffolk_County_Council' :                ["name":"Suffolk", "showLogo":true],
      'Surrey_County_Council' :                 ["name":"Surrey", "showLogo":true],
      'City_of_York' :                          ["name":"York", "showLogo":true],
      'London_Borough_of_Wandsworth_Council' :  ["name":"Wandsworth", "showLogo":true]
    ]

    def lookupProviderInformation(provider_shortcode) {
      members[provider_shortcode]
    }
}

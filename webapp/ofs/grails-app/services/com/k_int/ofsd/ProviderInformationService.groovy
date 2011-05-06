package com.k_int.ofsd

class ProviderInformationService {

    static transactional = true

    def members = [
      'Dorset__County_Council' :                ["name":"Dorset"],
      'Dorset_County_Council' :                 ["name":"Dorset"],  // This is how it is on test :(
      'Kent_County_Council' :                   ["name":"Kent"],
      'Luton_Borough_Council' :                 ["name":"Luton"],
      'North_Somerset_Council' :                ["name":"North Somerset"],
      'Oxfordshire_County_Council' :            ["name":"Oxfordshire"],
      'Rochdale_Metropolitan_Borough_Council' : ["name":"Rochdale"],
      'Sheffield_City_Council' :                ["name":"Sheffield"],
      'Southend-on-Sea_Borough_Council' :       ["name":"Southend"],
      'Suffolk_County_Council' :                ["name":"Suffolk"],
      'Surrey_County_Council' :                 ["name":"Surrey"],
      'City_of_York' :                          ["name":"York"],
      'London_Borough_of_Wandsworth_Council' :  ["name":"Wandsworth"]
    ]

    def lookupProviderInformation(provider_shortcode) {
      members[provider_shortcode]
    }
}

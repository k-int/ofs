package com.k_int.ofsd

class EntryController {

  def index = { 
    println "Entry: ${params.id}"
  }
}

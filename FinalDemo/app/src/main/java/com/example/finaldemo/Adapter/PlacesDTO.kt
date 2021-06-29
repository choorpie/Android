package com.example.finaldemo.Adapter

class PlacesDTO {
    var results = ArrayList<Results>()
}

class Results {
//    var geometry: Map<String, Viewport>? = null
    var name = ""
    lateinit var geometry: Map<String, LOCATION>
}

class LOCATION {
    var lat = ""
    var lng = ""
}

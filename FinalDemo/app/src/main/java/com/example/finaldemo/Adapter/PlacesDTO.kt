package com.example.finaldemo.Adapter

class PlacesDTO {
    var results = ArrayList<Results>()
}

class Results {
//    var geometry: Map<String, Viewport>? = null
    lateinit var geometry: Map<String, Viewport>
}

class Viewport {
    var lat = ""
    var lng = ""
}

package com.example.retrofitexample

/*
Define the data classes according to the JSON data format:
{
  "id": 1,
  "name": "Taipei",
  "towns": [
     {
       "id": 100,
       "name": "Zhongzheng District",
       "code": 100,
       "cwb_id": "6300500",
       "position": {
           "lat": "25.04214075",
           "lng": "121.5198716",
           "zoom": 9
        }
      }
   ]
 }
 */

data class City(val id: Int, val name: String, val towns: List<Town>)

data class Town(val id: Int, val name: String, val code: Int, val cwb_id: String, val position: Location)

data class Location(val lat: String, val lng: String, val zoom: Int)





package com.example.weatherforecast

import android.util.Log
import android.widget.ImageView
import androidx.core.net.toUri
import androidx.databinding.BindingAdapter
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class WeatherViewModel : ViewModel() {

    val cities = listOf<City>(
        City("Kaohsiung", "高雄市"),
        City("Taichung", "臺中市"),
        City("Tainan", "臺南市"),
        City("Taipei", "臺北市"),
        City("Taoyuan", "桃園市"),
        City("Chiayi", "嘉義市"),
        City("Hsinchu", "新竹市"),
        City("Keelung", "基隆市"),
        City("Changhua", "彰化市"),
        City("Douliu", "斗六市"),
        City("Hualien", "花蓮市"),
        City("Magong", "馬公市"),
        City("Miaoli", "苗栗市"),
        City("Nantou", "南投市"),
        City("Pingtung", "屏東市"),
        City("Puzi", "朴子市"),
        City("Taibao", "太保市"),
        City("Taitung", "臺東市"),
        City("Toufen", "頭份市"),
        City("Yilan", "宜蘭市"),
        City("Yuanlin", "員林市"),
        City("Zhubei", "竹北市")
    )

    //extract city names into two seperated lists
    val cities_ch = MutableList(cities.size, {cities[it].cName})
    val cities_en = MutableList(cities.size, {cities[it].eName})
    //add the selection hint text to be the first item
    init {
        cities_ch.add(0, "選擇一個城市")
        cities_en.add(0, "Select one city")
    }

    //livedata
    val selectedCityWeather = MutableLiveData<CityWeather>()

    //Callback version
//    fun sendRetrofitRequest(location: String) {
//        //send a https request
//        GetService.retrofitService.getAppData(location,"metric", "zh_tw", WeatherViewModel.API_KEY).enqueue(object:
//            Callback<WeatherData> {
//            override fun onResponse(call: Call<WeatherData>, response: Response<WeatherData>) {
//                response.body()?.let {
//                    //update UI
//                    val temp = CityWeather(
//                   it.name,
//                   it.main.temp,
//                   it.weather[0].description,
//                   it.weather[0].icon
//               )
//               selectedCityWeather.value = temp
//                    //Log.d("Main", response.body().toString())
//                }
//            }
//            override fun onFailure(call: Call<WeatherData>, t: Throwable) {
//                Log.d("Main", t.message.toString())
//            }
//        })
//    }

    //Coroutine version
    fun sendRetrofitRequest(location: String) {
        viewModelScope.launch {
            try {
                val result =
                    GetService.retrofitService.getAppData(location, "metric", "zh_tw", API_KEY)
                val temp = CityWeather(
                    result.name,
                    result.main.temp,
                    result.weather[0].description,
                    result.weather[0].icon
                )
                selectedCityWeather.value = temp
                Log.d("Main", temp.toString())
            } catch (e: Exception) {
                Log.d("Main", "Fail to access: ${e.message}")
            }
        }
    }

    companion object {  //static global constants
        const val API_URL = "https://api.openweathermap.org/"
        const val ICON_URL = "https://openweathermap.org/img/wn/"
        const val API_KEY = "YOUR_API_KEY"
    }
}


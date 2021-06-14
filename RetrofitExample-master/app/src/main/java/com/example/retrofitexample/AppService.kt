package com.example.retrofitexample

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import retrofit2.http.GET

//use the http: add android:usesCleartextTraffic="true" in AndroidManifest
//test URL1: http://web.csie.ndhu.edu.tw/sclo/course.htm
//test URL2: http://web.csie.ndhu.edu.tw/sclo/programming/taipei_towns.json

private const val BASE_URL =
    //"http://web.csie.ndhu.edu.tw/sclo/"
    "http://web.csie.ndhu.edu.tw/sclo/programming/"

private val moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()

private val retrofit = Retrofit.Builder()
                .baseUrl(BASE_URL)
                //.addConverterFactory(ScalarsConverterFactory.create())
               //.addConverterFactory(GsonConverterFactory.create())
               .addConverterFactory(MoshiConverterFactory.create(moshi))
              .build()

interface AppService {
    //get a webpage in text format (string value)
    //@GET("course.htm")
    //suspend fun getAppData(): String  //coroutine version

    //@GET("taipei_towns.json")
    //get a JSON file using the Call object
    //fun getAppData(): Call<City>  //List<City> if having an array of cities

    @GET("taipei_towns.json")
    //get a JSON file using the coroutine
    suspend fun getAppData(): City  //coroutine version
}

//singleton object that is used globally
object GetService {
    val retrofitService : AppService by lazy {
        retrofit.create(AppService::class.java) }
}
package com.example.retrofitexample

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.retrofitexample.databinding.ActivityMainBinding
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/*
The app demo the usage of Retrofit
 */

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.button.setOnClickListener { sendRetrofitRequest() }
    }

    //callback version
//    fun sendRetrofitRequest() {
//        GetService.retrofitService.getAppData().enqueue(object : Callback<City> {
//            override fun onResponse(call: Call<City>, response: Response<City>) {
//                binding.textView.text = response.body().toString()
//                Log.d("Main", response.body().toString())
//            }
//            override fun onFailure(call: Call<City>, t: Throwable) {
 //                Log.d("Main", "Fail to access: ${e.message}")
//            }
//        })
//    }

    //coroutine version
    fun sendRetrofitRequest() {
        val job = Job()
        //create a coroutine scope using the I/O threads
        val scope = CoroutineScope(Dispatchers.IO+job)
        scope.launch {
            try {
                val result = GetService.retrofitService.getAppData()
                withContext(Dispatchers.Main) { //switch to the UI thread
                    binding.textView.text = result.toString()
                }
            } catch (e: Exception) {
                Log.d("Main", "Fail to access: ${e.message}")
            }
        }
    }
}
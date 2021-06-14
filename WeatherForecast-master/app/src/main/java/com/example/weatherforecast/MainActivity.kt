package com.example.weatherforecast

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.example.weatherforecast.databinding.ActivityMainBinding

/*
* This App requires the setting: minSdkVersion 21
* Before you can run this app, apply your own api key
* and change the definition: const val API_KEY = "YOUR_API_KEY" in viewmodel
 */

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    lateinit var viewModel: WeatherViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //get the layout binding object
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        //get the viewModel
        viewModel = ViewModelProvider(this).get(WeatherViewModel::class.java)
        //configure the layout data binding
        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        //configure the spinner
        // data source comes from an array
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, viewModel.cities_ch)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinner.adapter = adapter
        //item selection handler
        binding.spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val selectedPosition = parent?.selectedItemPosition
                selectedPosition?.let {
                    if (it != 0) { //not the first item (hint text only)
                        //send an Internet request to the weather website
                        viewModel.sendRetrofitRequest(viewModel.cities_en[it])
                    }
                    else {
                        viewModel.selectedCityWeather.value = null
                    }
                }
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }
    }

}
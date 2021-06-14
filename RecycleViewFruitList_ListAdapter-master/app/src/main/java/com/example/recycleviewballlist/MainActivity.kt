package com.example.recycleviewballlist

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.recycleviewballlist.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        val viewModel = ViewModelProvider(this).get(MyViewModel::class.java)

        //configure the recylerview
        val layoutManager = LinearLayoutManager(this)
        binding.recycleView.layoutManager = layoutManager
        val adapter = BallAdapter() //need not pass the data source initially
        binding.recycleView.adapter = adapter
        binding.recycleView.addItemDecoration(DividerItemDecoration(this, DividerItemDecoration.VERTICAL))

        //observe any changes on the data source
        viewModel.ballList.observe(this, Observer {
            it?.let {
                adapter.submitList(it)  //submit the up-to-date ballList to the recyclerView
            }
        })
    }
}

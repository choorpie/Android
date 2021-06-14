package com.example.recycleviewballlist

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.SearchView
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.recycleviewballlist.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val ballList = ArrayList<Balls>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        initBallList()
        //configure the recyclerview
        val layoutManager = LinearLayoutManager(this)
        binding.recycleView.layoutManager = layoutManager
        val adapter = BallAdapter(ballList)
        binding.recycleView.adapter = adapter
        binding.recycleView.addItemDecoration(DividerItemDecoration(this, DividerItemDecoration.VERTICAL))

        //configure the searchview
        binding.searchBox.setOnQueryTextListener(object: SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }
            override fun onQueryTextChange(newText: String?): Boolean {
                adapter.filter.filter(newText)
                return true
            }
        })
    }

    private fun initBallList() {
        repeat(6) {
            ballList.add(Balls("Baseball",R.drawable.baseball))
            ballList.add(Balls("Basketball",R.drawable.basketball))
            ballList.add(Balls("Football",R.drawable.football))
            ballList.add(Balls("Other",R.drawable.other))
        }
    }
}

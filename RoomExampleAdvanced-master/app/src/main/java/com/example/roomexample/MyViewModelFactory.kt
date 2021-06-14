package com.example.roomexample

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.roomexample.database.SceneDatabaseDao

//need the factory to pass an argument to the viewmodel
class MyViewModelFactory(private val dataSource: SceneDatabaseDao): ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        @Suppress("unchecked_cast")
        return MyViewModel(dataSource) as T
    }
}
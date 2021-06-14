package com.example.roomexample.database

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface SceneDatabaseDao {
    @Insert
    suspend fun insertScene(scene: Scene) //insert the scene specified by the parameter

    @Delete
    suspend fun deleteScene(scene: Scene) //delete the scene specified by the parameter

    @Query("select * from Scene where id = :id")
    suspend fun loadOneScene(id: Long): Scene

    @Query("select * from Scene") //load all user data
    fun loadAllScenes(): LiveData<List<Scene>> //should be livedata in the project

    @Query ("delete from Scene")
    suspend fun deleleAllScenes()
}
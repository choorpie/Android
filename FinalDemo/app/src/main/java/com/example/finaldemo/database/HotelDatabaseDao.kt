package com.example.finaldemo.database

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface HotelDatabaseDao {
    @Insert
    suspend fun insertHotel(hotel: Hotel) //insert the hotel specified by the parameter

    @Update
    suspend fun updateHotel(Hotel: Hotel)

    @Delete
    suspend fun deleteHotel(hotel: Hotel) //delete the hotel specified by the parameter

    @Query("select * from Hotel where id = :id")
    suspend fun loadOneHotel(id: Long): Hotel

    @Query("select * from Hotel") //load all user data
    fun loadAllHotels(): LiveData<List<Hotel>> //should be livedata in the project

    @Query("select * from Hotel where name LIKE '%' || :name || '%'") // find matched hotels by names
    fun findHotels(name: String): LiveData<List<Hotel>> // should be livedata in the project

    @Query("delete from Hotel")
    suspend fun deleleAllHotels()
}
package com.example.roomexample

import androidx.room.*

//the mapping between functions and database queries
@Dao
interface UserDao {
    @Insert
    fun insertUser(user: User): Long //insert the user specified by the parameter

    @Update
    fun updateUser(newUser: User) //update the data for the user given by the parameter

    @Query("select * from User") //load all user data
    fun loadAllUsers(): List<User>

    @Query("select user_name from User where age > :age")  //Show the user names that are qualified
    fun loadUserOlderThan(age: Int): List<String>

    @Delete
    fun deleteUser(user: User) // //delete the user specified by the parameter

    @Query ("delete from User")
    fun deleleAllUser()
}
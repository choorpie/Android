package com.example.roomexample

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class User (
    @ColumnInfo(name = "user_name")  //customized column field name
    var name: String,
    var age: Int
) {
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0L
}
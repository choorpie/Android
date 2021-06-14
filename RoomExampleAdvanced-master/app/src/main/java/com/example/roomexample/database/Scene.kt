package com.example.roomexample.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Scene (
    var city: String,
    var name: String,
    var photoId: Int,
    var description: String
) {
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0L
    var photoFile: String = ""  //for the external photo file
}

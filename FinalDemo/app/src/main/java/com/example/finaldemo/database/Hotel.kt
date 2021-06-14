package com.example.finaldemo.database

import android.location.Address
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Hotel (
    var city: String,
    var name: String,
//    var address: Address,
    var photoId: Int,
    var description: String,
    var mapId: Int,
    var phoneNuber: String
) {
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0L
    var photoFile: String = ""  //for the external photo file
}

package com.example.TravApp.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(tableName = "hotel")
data class Hotel(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "hotel_id") val hotelId: Int = 0,
    @ColumnInfo(name = "tripId") val tripId: Long,
    val name: String,
    val address: String,
    val checkInDate: String,
    val checkOutDate: String,
    val checkInTime: String,
    val checkOutTime: String
)



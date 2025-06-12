package com.example.TravApp.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "trip")
data class Trip(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "trip_id") val tripId: Long = 0,
    @ColumnInfo(name = "title") val title: String,
    @ColumnInfo(name = "start_date") val start_date: String,
    @ColumnInfo(name = "end_date") val end_date: String,
)






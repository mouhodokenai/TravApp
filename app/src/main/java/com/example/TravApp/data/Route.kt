package com.example.TravApp.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import androidx.room.TypeConverters

import androidx.room.*
import java.util.*


@Entity(tableName = "route",
    foreignKeys = [
        ForeignKey(
            entity = Trip::class,
            parentColumns = ["trip_id"],
            childColumns = ["tripId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index(value = ["tripId"])]
)
data class Route(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "route_id") val routeId: Int = 0,
    @ColumnInfo(name = "tripId") val tripId: Long,
    @ColumnInfo(name = "place") val placeName: String,
    @ColumnInfo(name = "latitude") val latitude: Double,
    @ColumnInfo(name = "longitude") val longitude: Double,
    @ColumnInfo(name = "orderIndex") val orderIndex: Int? = null
)


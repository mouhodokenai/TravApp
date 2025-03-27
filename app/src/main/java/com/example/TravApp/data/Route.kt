package com.example.TravApp.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import androidx.room.TypeConverters

import androidx.room.*
import java.util.*

@Entity(
    tableName = "route",
    foreignKeys = [
        ForeignKey(
            entity = Trip::class, // Предполагается, что есть класс Trip
            parentColumns = ["trip_id"],
            childColumns = ["trip_id"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index(value = ["trip_id"])]
)
data class Route(

    @PrimaryKey val routeId: UUID = UUID.randomUUID(), // ID маршрута (UUID)
    @ColumnInfo(name = "trip_id") val tripId: UUID, // FK на поездку
    @ColumnInfo(name = "place_name") val placeName: String, // Название места
    @ColumnInfo(name = "latitude") val latitude: Double, // Широта
    @ColumnInfo(name = "longitude") val longitude: Double, // Долгота
    @ColumnInfo(name = "arrival_time") val arrivalTime: String, // Время прибытия (в формате ISO 8601)
    @ColumnInfo(name = "departure_time") val departureTime: String, // Время отправления (в формате ISO 8601)
    @ColumnInfo(name = "order_index") val orderIndex: Int // Порядок точек
)

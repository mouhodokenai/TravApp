package com.example.TravApp.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.UUID

@Entity(tableName = "trips")
data class Trip(
    @PrimaryKey val trip_id: UUID = UUID.randomUUID(),  // Уникальный ID поездки
    val title: String,  // Название поездки
    val start_date: String,  // Дата начала поездки
    val end_date: String,  // Дата окончания поездки
    val created_at: String = System.currentTimeMillis().toString()  // Текущее время в миллисекундах
)

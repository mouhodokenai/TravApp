package com.example.TravApp.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import androidx.room.TypeConverters

@Entity(tableName = "route")
@TypeConverters(RouteConverters::class) // Указываем, что в этой таблице используется конвертер типов
data class Route(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,  // Уникальный ID маршрута
    val name: String,  // Название маршрута
    val points: List<String> // Список точек маршрута
)

// Конвертер для хранения списка строк в базе данных
class RouteConverters {
    @TypeConverter
    fun fromList(value: List<String>): String {
        return value.joinToString(separator = ",") // Преобразуем список в строку, разделяя запятыми
    }

    @TypeConverter
    fun toList(value: String): List<String> {
        return value.split(",") // Преобразуем строку обратно в список
    }
}

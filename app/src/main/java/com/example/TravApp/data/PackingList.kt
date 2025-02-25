package com.example.TravApp.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.UUID

@Entity(tableName = "packing_list")
data class PackingList(
    @PrimaryKey val item_id: UUID = UUID.randomUUID(),  // Уникальный ID вещи
    val trip_id: UUID,  // Ссылка на поездку
    val name: String,  // Название вещи
    val is_packed: Boolean = false  // Отметка, упакована ли вещь
)

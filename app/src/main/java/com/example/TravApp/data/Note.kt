package com.example.TravApp.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.UUID

@Entity(tableName = "notes")
data class Note(
    @PrimaryKey val note_id: UUID = UUID.randomUUID(),  // Уникальный ID заметки
    val trip_id: UUID,  // Ссылка на поездку
    val content: String,  // Содержание заметки
    val created_at: String = System.currentTimeMillis().toString()  // Текущее время в миллисекундах
)

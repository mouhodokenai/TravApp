package com.example.TravApp.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.UUID

@Entity(tableName = "tickets")
data class Ticket(
    @PrimaryKey val ticket_id: UUID = UUID.randomUUID(),  // Уникальный ID билета
    val trip_id: UUID,  // Ссылка на поездку
    val transport_type: String,  // Тип транспорта
    val departure_city: String,  // Город отправления
    val arrival_city: String,  // Город прибытия
    val departure_time: String,  // Время отправления
    val arrival_time: String,  // Время прибытия
    val ticket_number: String  // Номер билета
)


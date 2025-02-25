package com.example.TravApp.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.UUID

@Entity(tableName = "budget")
data class Budget(
    @PrimaryKey val budget_id: UUID = UUID.randomUUID(),  // Уникальный ID бюджета
    val trip_id: UUID,  // Ссылка на поездку
    val category: String,  // Категория расходов
    val planned_amount: Double,  // Запланированная сумма
    val actual_amount: Double  // Потраченная сумма
)

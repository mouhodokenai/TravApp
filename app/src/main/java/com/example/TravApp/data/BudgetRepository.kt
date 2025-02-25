package com.example.TravApp.data

import com.example.TravApp.data.Trip
import com.example.TravApp.data.TripDao
import kotlinx.coroutines.flow.Flow

class BudgetRepository(private val budgetDao: BudgetDao) {

    // Получение всех записей бюджета для конкретной поездки
    /*
    fun getBudgetByTripId(tripId: String): Flow<List<Budget>> {
        return budgetDao.getBudgetByTripId(tripId)
    }
    */

    // Добавление записи бюджета
    suspend fun insertBudget(budget: Budget) {
        budgetDao.insert(budget)
    }

    // Удаление записи бюджета
    suspend fun deleteBudget(budget: Budget) {
        budgetDao.delete(budget)
    }

    // Обновление записи бюджета
    suspend fun updateBudget(budget: Budget) {
        budgetDao.update(budget)
    }
}



package com.example.TravApp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.TravApp.data.Budget
import com.example.TravApp.data.BudgetRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class BudgetViewModel(private val repository: BudgetRepository) : ViewModel() {

    // Получение бюджета по ID поездки
    /*
    fun getBudgetByTripId(tripId: String): Flow<List<Budget>> {
        return repository.getBudgetByTripId(tripId)
    }

     */

    // Добавление записи в бюджет
    fun addBudget(budget: Budget) {
        viewModelScope.launch {
            repository.insertBudget(budget)
        }
    }

    // Удаление записи бюджета
    fun removeBudget(budget: Budget) {
        viewModelScope.launch {
            repository.deleteBudget(budget)
        }
    }

    // Обновление записи бюджета
    fun updateBudget(budget: Budget) {
        viewModelScope.launch {
            repository.updateBudget(budget)
        }
    }
}

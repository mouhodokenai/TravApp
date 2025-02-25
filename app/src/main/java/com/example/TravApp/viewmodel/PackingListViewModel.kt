package com.example.TravApp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.TravApp.data.PackingList
import com.example.TravApp.data.PackingListRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class PackingListViewModel(private val repository: PackingListRepository) : ViewModel() {

    // Получение списка вещей по ID поездки
    /*
    fun getPackingListByTripId(tripId: String): Flow<List<PackingList>> {
        return repository.getPackingListByTripId(tripId)
    }

     */

    // Добавление вещи
    fun addItem(item: PackingList) {
        viewModelScope.launch {
            repository.insert(item)
        }
    }

    // Обновление вещи
    fun updateItem(item: PackingList) {
        viewModelScope.launch {
            repository.update(item)
        }
    }

    // Удаление вещи
    fun removeItem(item: PackingList) {
        viewModelScope.launch {
            repository.delete(item)
        }
    }
}

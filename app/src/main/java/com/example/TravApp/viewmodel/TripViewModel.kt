package com.example.TravApp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.TravApp.data.Trip
import com.example.TravApp.data.TripRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class TripViewModel(private val repository: TripRepository) : ViewModel() {

    // Получение списка всех поездок
    /*
    fun getAllTrips(): Flow<List<Trip>> {
        return repository.getAllTrips()
    }

     */

    // Добавление новой поездки
    fun addTrip(trip: Trip) {
        viewModelScope.launch {
            repository.insertTrip(trip)
        }
    }

    // Обновление поездки
    fun updateTrip(trip: Trip) {
        viewModelScope.launch {
            repository.updateTrip(trip)
        }
    }

    // Удаление поездки
    fun removeTrip(trip: Trip) {
        viewModelScope.launch {
            repository.deleteTrip(trip)
        }
    }
}

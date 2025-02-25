package com.example.TravApp.data

import com.example.TravApp.data.Trip
import com.example.TravApp.data.TripDao
import kotlinx.coroutines.flow.Flow

class TripRepository(private val tripDao: TripDao) {

    // Получение всех поездок в виде потока Flow
    suspend fun getAllTrips(): Flow<List<Trip>> = tripDao.getAllTrips()

    // Добавление поездки
    suspend fun insertTrip(trip: Trip) {
        tripDao.insert(trip)
    }

    // Удаление поездки
    suspend fun deleteTrip(trip: Trip) {
        tripDao.delete(trip)
    }

    // Обновление данных о поездке
    suspend fun updateTrip(trip: Trip) {
        tripDao.update(trip)
    }
}

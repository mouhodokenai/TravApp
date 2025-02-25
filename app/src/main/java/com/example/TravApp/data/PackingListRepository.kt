package com.example.TravApp.data

import kotlinx.coroutines.flow.Flow

class PackingListRepository(private val packingListDao: PackingListDao) {

    // Получение всех вещей для конкретной поездки
    /*
    fun getPackingListByTripId(tripId: String): Flow<List<PackingList>> {
        return packingListDao.getPackingListByTripId(tripId)
    }
    */

    // Добавление вещи в список
    suspend fun insert(item: PackingList) {
        packingListDao.insert(item)
    }

    // Обновление информации о вещи
    suspend fun update(item: PackingList) {
        packingListDao.update(item)
    }

    // Удаление вещи из списка
    suspend fun delete(item: PackingList) {
        packingListDao.delete(item)
    }
}

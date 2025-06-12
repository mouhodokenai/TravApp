package com.example.TravApp.data

class HotelRepository(private val hotelDao: HotelDao) {

    suspend fun getTripBudgets(tripId: Long): List<Hotel> {
        return hotelDao.getHotelsForTrip(tripId)
    }

    suspend fun insert(hotel: Hotel) {
        hotelDao.insert(hotel)
    }

    suspend fun update(hotel: Hotel) {
        hotelDao.update(hotel)
    }

    suspend fun delete(hotel: Hotel) {
        hotelDao.delete(hotel)
    }
}
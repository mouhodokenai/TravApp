package com.example.TravApp.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface HotelDao {
    @Insert
    suspend fun insert(hotel: Hotel)

    @Update
    suspend fun update(hotel: Hotel)

    @Delete
    suspend fun delete(hotel: Hotel)

    @Query("SELECT * FROM hotel WHERE tripId = :tripId")
    suspend fun getHotelsForTrip(tripId: Long): List<Hotel>

    @Query("DELETE FROM hotel")
    suspend fun deleteAllHotels()
}


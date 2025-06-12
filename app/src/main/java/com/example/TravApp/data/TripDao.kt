package com.example.TravApp.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow
import java.util.UUID

@Dao
interface TripDao {
    @Insert
    suspend fun insert(trip: Trip): Long

    @Update
    suspend fun update(trip: Trip)

    @Delete
    suspend fun delete(trip: Trip)

    @Query("SELECT * FROM trip")
    suspend fun getAllTrips(): List<Trip>

    @Query("SELECT * FROM trip")
    fun getAllTrips2(): Flow<List<Trip>>

    @Query("SELECT * FROM trip WHERE trip_id = :tripId")
    suspend fun getByTripId(tripId: Long): Trip

    @Query("DELETE FROM trip")
    suspend fun deleteAllTrips()
}



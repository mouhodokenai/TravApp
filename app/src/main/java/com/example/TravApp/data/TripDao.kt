package com.example.TravApp.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import java.util.UUID

@Dao
interface TripDao {
    @Insert
    suspend fun insert(trip: Trip)

    @Update
    suspend fun update(trip: Trip)

    @Delete
    suspend fun delete(trip: Trip)

    @Query("SELECT * FROM trips WHERE trip_id = :tripId")
    suspend fun getTripById(tripId: UUID): Trip?
}

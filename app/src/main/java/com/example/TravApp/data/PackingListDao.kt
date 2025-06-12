package com.example.TravApp.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import java.util.UUID

@Dao
interface PackingListDao {
    @Insert
    suspend fun insert(item: PackingList)

    @Update
    suspend fun update(item: PackingList)

    @Delete
    suspend fun delete(item: PackingList)

    @Query("SELECT * FROM packing_list")
    suspend fun getAll(): List<PackingList>

    @Query("SELECT * FROM packing_list WHERE trip_id = :tripId")
    suspend fun getByTripId(tripId: Long): List<PackingList>

    @Query("DELETE FROM trip")
    suspend fun deleteAllPackingItems()
}



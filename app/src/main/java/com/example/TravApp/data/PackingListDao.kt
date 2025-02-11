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
    suspend fun insert(trip: PackingList)

    @Update
    suspend fun update(trip: PackingList)

    @Delete
    suspend fun delete(trip: PackingList)

    @Query("SELECT * FROM packingList WHERE item_id = :itemId") //???
    suspend fun getTicketById(tripId: UUID): PackingList?  //???
}
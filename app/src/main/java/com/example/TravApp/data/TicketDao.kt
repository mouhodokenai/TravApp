package com.example.TravApp.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import java.util.UUID

@Dao
interface TicketDao {
    @Insert
    suspend fun insert(trip: Ticket)

    @Update
    suspend fun update(trip: Ticket)

    @Delete
    suspend fun delete(trip: Ticket)

    @Query("SELECT * FROM tikets WHERE tiket_id = :tripId")
    suspend fun getTicketById(tripId: UUID): Ticket?
}
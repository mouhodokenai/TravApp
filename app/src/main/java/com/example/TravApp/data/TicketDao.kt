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
    suspend fun insert(ticket: Ticket)

    @Update
    suspend fun update(ticket: Ticket)

    @Delete
    suspend fun delete(ticket: Ticket)

    @Query("SELECT * FROM tickets WHERE trip_id = :tripId")
    suspend fun getByTripId(tripId: Long): List<Ticket>

    @Query("DELETE FROM tickets")
    suspend fun deleteAllTickets()
}





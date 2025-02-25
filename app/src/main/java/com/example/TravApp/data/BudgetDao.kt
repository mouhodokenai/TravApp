package com.example.TravApp.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import java.util.UUID

@Dao
interface BudgetDao {
    @Insert
    suspend fun insert(trip: Budget)

    @Update
    suspend fun update(trip: Budget)

    @Delete
    suspend fun delete(trip: Budget)

    @Query("SELECT * FROM budget WHERE budget_id = :id")
    suspend fun getTicketById(tripId: UUID): Budget?
}
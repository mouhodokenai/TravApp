package com.example.TravApp.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update


@Dao
interface BudgetDao {
    @Insert
    suspend fun insert(budget: Budget)

    @Update
    suspend fun update(budget: Budget)

    @Delete
    suspend fun delete(budget: Budget)

    @Query("SELECT * FROM budget")
    suspend fun getAll(): List<Budget>

    @Query("SELECT * FROM budget WHERE trip_id = :tripId")
    suspend fun getByTripId(tripId: Long): List<Budget>

    @Query("DELETE FROM budget")
    suspend fun deleteAllBudgets()
}







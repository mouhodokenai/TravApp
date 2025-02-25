package com.example.TravApp.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import java.util.UUID

@Dao
interface NoteDao {
    @Insert
    suspend fun insert(trip: Note)

    @Update
    suspend fun update(trip: Note)

    @Delete
    suspend fun delete(trip: Note)

}
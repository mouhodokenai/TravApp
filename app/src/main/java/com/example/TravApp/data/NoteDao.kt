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
    suspend fun insert(note: Note)

    @Update
    suspend fun update(note: Note)

    @Delete
    suspend fun delete(note: Note)

    @Query("SELECT * FROM notes WHERE trip_id = :tripId")
    suspend fun getByTripId(tripId: Long): List<Note>

    @Query("SELECT * FROM notes")
    suspend fun getAll(): List<Note>

    @Query("DELETE FROM notes")
    suspend fun deleteAllNotes()
}



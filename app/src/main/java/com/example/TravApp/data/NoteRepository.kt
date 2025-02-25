package com.example.TravApp.data

import androidx.lifecycle.LiveData
import kotlinx.coroutines.flow.Flow

class NoteRepository(private val noteDao: NoteDao) {

    // Получение всех заметок для конкретной поездки
    //fun getNotesByTripId(tripId: String): Flow<List<Note>> {
        //return noteDao.getNotesByTripId(tripId)
    //}

    // Добавление заметки
    suspend fun insert(note: Note) {
        noteDao.insert(note)
    }

    // Обновление заметки
    suspend fun update(note: Note) {
        noteDao.update(note)
    }

    // Удаление заметки
    suspend fun delete(note: Note) {
        noteDao.delete(note)
    }
}
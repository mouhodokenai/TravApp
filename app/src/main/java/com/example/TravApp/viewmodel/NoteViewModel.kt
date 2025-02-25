package com.example.TravApp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.TravApp.data.Note
import com.example.TravApp.data.NoteRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class NoteViewModel(private val repository: NoteRepository) : ViewModel() {

    // Получение списка заметок по ID поездки
    /*fun getNotesByTripId(tripId: String): Flow<List<Note>> {
        return repository.getNotesByTripId(tripId)
    }

     */

    // Добавление заметки
    fun addNote(note: Note) {
        viewModelScope.launch {
            repository.insert(note)
        }
    }

    // Обновление заметки
    fun updateNote(note: Note) {
        viewModelScope.launch {
            repository.update(note)
        }
    }

    // Удаление заметки
    fun removeNote(note: Note) {
        viewModelScope.launch {
            repository.delete(note)
        }
    }
}

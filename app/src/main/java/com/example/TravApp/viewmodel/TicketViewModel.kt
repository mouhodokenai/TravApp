package com.example.TravApp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.TravApp.data.Ticket
import com.example.TravApp.data.TicketRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class TicketViewModel(private val repository: TicketRepository) : ViewModel() {

    // Получение списка билетов по ID поездки
    /*
    fun getTicketsByTripId(tripId: String): Flow<List<Ticket>> {
        return repository.getTicketsByTripId(tripId)
    }

     */

    // Добавление билета
    fun addTicket(ticket: Ticket) {
        viewModelScope.launch {
            repository.insert(ticket)
        }
    }

    // Обновление билета
    fun updateTicket(ticket: Ticket) {
        viewModelScope.launch {
            repository.update(ticket)
        }
    }

    // Удаление билета
    fun removeTicket(ticket: Ticket) {
        viewModelScope.launch {
            repository.delete(ticket)
        }
    }
}

package com.example.TravApp.data

import kotlinx.coroutines.flow.Flow

class TicketRepository(private val ticketDao: TicketDao) {

    // Получение всех билетов для конкретной поездки
    /*
    fun getTicketsByTripId(tripId: String): Flow<List<Ticket>> {
        return ticketDao.getTicketsByTripId(tripId)
    }

     */

    // Добавление билета
    suspend fun insert(ticket: Ticket) {
        ticketDao.insert(ticket)
    }

    // Обновление билета
    suspend fun update(ticket: Ticket) {
        ticketDao.update(ticket)
    }

    // Удаление билета
    suspend fun delete(ticket: Ticket) {
        ticketDao.delete(ticket)
    }
}

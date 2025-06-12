package com.example.testapp

import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.example.TravApp.data.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.*
import org.junit.Assert.*
import org.junit.runner.RunWith

@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
class AppDaoUnitTest {

    private lateinit var database: TravDatabase
    private lateinit var budgetDao: BudgetDao
    private lateinit var ticketDao: TicketDao
    private lateinit var noteDao: NoteDao
    private lateinit var hotelDao: HotelDao
    private lateinit var routeDao: RouteDao

    @Before
    fun setUp() {
        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            TravDatabase::class.java
        ).allowMainThreadQueries().build()

        budgetDao = database.budgetDao()
        ticketDao = database.ticketDao()
        noteDao = database.noteDao()
        hotelDao = database.hotelDao()
        routeDao = database.routeDao()
    }

    @After
    fun tearDown() {
        database.close()
    }

    // === БЮДЖЕТ ===

    @Test
    fun testAddValidBudget() = runTest {
        val budget = Budget(1, 1, "Еда", 500.0, "₽")
        budgetDao.insert(budget)
        val result = budgetDao.getByTripId(1)
        assertTrue(result.contains(budget))
    }


    // === БИЛЕТ ===

    @Test
    fun testAddValidTicket() = runTest {
        val tickeнt = Ticket(1, 1, "Автобус", "Пенза",
            "Москва", "2025-07-01T10:00",
            "2025-07-01T16:00", "987654")
        ticketDao.insert(ticket)
        val result = ticketDao.getByTripId(1)
        assertEquals(1, result.size)
        assertEquals("Автобус", result[0].transport_type)
    }


    // === ОТЕЛЬ ===

    @Test
    fun testAddHotel() = runTest {
        val hotel = Hotel(1, 1, "2025-07-10",
            "Ладожская 34", "2025-07-14", "2025-07-15")
        hotelDao.insert(hotel)
        val result = hotelDao.getHotelsForTrip(1)
        assertEquals(1, result.size)
        assertEquals("2025-07-10", result[0].checkIn)
    }

    // === ЗАМЕТКА ===

    @Test
    fun testAddNote() = runTest {
        val note = Note(1, 1, "карту метро", "купить карту метро")
        noteDao.insert(note)
        val result = noteDao.getByTripId(1)
        assertEquals("купить карту метро", result[0].content)
    }

    // === МАРШРУТ ===

    @Test
    fun testAddRoute() = runTest {
        val route = Route(1, 1, "Галатская башня", 41.0256, 28.9744)
        routeDao.insert(route)
        val routes = routeDao.getRoutesForTrip(1)
        assertTrue(routes.isNotEmpty())
        assertEquals("Галатская башня", routes[0].placeName)
    }
}

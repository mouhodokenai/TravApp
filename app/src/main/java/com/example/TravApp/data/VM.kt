package com.example.TravApp.data

import android.app.Application
import android.content.Context
import android.location.Geocoder
import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.text.ParseException
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.util.Calendar
import java.util.Collections
import java.util.Locale
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.withContext
import kotlinx.coroutines.withContext



class TravViewModel(application: Application) : AndroidViewModel(application) {

    private val db = TravDatabase.getInstance(application)

    private val routeDao: RouteDao = db.routeDao()
    private val tripDao: TripDao = db.tripDao()
    private val ticketDao: TicketDao = db.ticketDao()
    private val budgetDao: BudgetDao = db.budgetDao()
    private val packingListDao: PackingListDao = db.packingListDao()
    private val noteDao: NoteDao = db.noteDao()
    private val hotelDao: HotelDao = db.hotelDao()

    private val _currentTrip = mutableStateOf<Trip?>(null)
    val currentTrip: State<Trip?> = _currentTrip

    private val _tripItems = MutableLiveData<List<Trip>>()
    val tripItems: LiveData<List<Trip>> = _tripItems

    private val _budgetItems = MutableLiveData<List<Budget>>()
    val budgetItems: LiveData<List<Budget>> = _budgetItems

    private val _ticketItems = MutableLiveData<List<Ticket>>()
    val ticketItems: LiveData<List<Ticket>> = _ticketItems

    private val _packingListItems = MutableLiveData<List<PackingList>>()
    val packingListItems: LiveData<List<PackingList>> = _packingListItems

    private val _noteItems = MutableLiveData<List<Note>>()
    val noteItems: LiveData<List<Note>> = _noteItems

    private val _routeItems = MutableLiveData<List<Route>>()
    val routeItems: LiveData<List<Route>> = _routeItems

    private val _hotelItems = MutableLiveData<List<Hotel>>()
    val hotelItems: LiveData<List<Hotel>> = _hotelItems

    private val _selectedPlace = MutableLiveData<Route?>()
    val selectedPlace: LiveData<Route?> = _selectedPlace

    val todayMillis = System.currentTimeMillis()

    val dateFormat = SimpleDateFormat("EEE MMM dd HH:mm:ss z yyyy", Locale.ENGLISH)

    fun clearDatabase() {
        viewModelScope.launch {
            tripDao.deleteAllTrips()
            routeDao.deleteAllRoutes()
            ticketDao.deleteAllTickets()
            hotelDao.deleteAllHotels()
            budgetDao.deleteAllBudgets()
            packingListDao.deleteAllPackingItems()
            noteDao.deleteAllNotes()
        }
    }

    fun getStats(context: Context): Flow<Stats> =
        combine(
            tripDao.getAllTrips2(),
            routeDao.getAllRoutes2()
        ) { trips, routes ->
            Log.d("StatsFlow", "Trips: ${trips.size}, Routes: ${routes.size}")

            val tripCount = trips.size
            val cityCount = routes.map { it.placeName }.distinct().size

            val geocoder = Geocoder(context, Locale.getDefault())
            val countryNames = routes.mapNotNull { route ->
                try {
                    val addresses = geocoder.getFromLocation(route.latitude, route.longitude, 1)
                    addresses?.firstOrNull()?.countryName
                } catch (e: Exception) {
                    null
                }
            }.toSet()

            Log.d("StatsFlow", "Country count: ${countryNames.size}")

            Stats(
                tripCount = tripCount,
                cityCount = cityCount,
                countryCount = countryNames.size
            )
        }




    fun dateToMillis(dateString: String): Long {
        return try {
            val date = dateFormat.parse(dateString)
            date?.time ?: 0L
        } catch (e: ParseException) {
            Log.e("TripsDebug", "Ошибка парсинга даты: $dateString", e)
            0L
        }
    }

    val todayStartMillis: Long = Calendar.getInstance().apply {
        set(Calendar.HOUR_OF_DAY, 0)
        set(Calendar.MINUTE, 0)
        set(Calendar.SECOND, 0)
        set(Calendar.MILLISECOND, 0)
    }.timeInMillis

    val upcomingTrips: StateFlow<List<TripWithRoutes>> = tripDao.getAllTrips2()
        .map { trips ->
            Log.d("TripsDebug", "Всего поездок из БД: ${trips.size}")

            val filtered = trips.filter { trip ->
                val tripMillis = dateToMillis(trip.end_date)
                Log.d("TripsDebug", "Поездка: ${trip.title}, end_date: ${trip.end_date}, millis: $tripMillis, todayStartMillis: $todayStartMillis")
                tripMillis >= todayStartMillis
            }

            Log.d("TripsDebug", "Подходящих поездок: ${filtered.size}")
            filtered
        }
        .map { trips ->
            trips.map { trip ->
                val locations = routeDao.getRoutesForTrip(trip.tripId)
                    .take(3)
                    .map { it.placeName }
                Log.d("TripsDebug", "Поездка: ${trip.title}, места: $locations")
                TripWithRoutes(trip, locations)
            }
        }
        .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    val comingTrips: StateFlow<List<TripWithRoutes>> = tripDao.getAllTrips2()
        .map { trips ->
            trips.filter { trip ->
                dateToMillis(trip.end_date) < todayStartMillis
            }
        }
        .map { trips ->
            trips.map { trip ->
                val locations = routeDao.getRoutesForTrip(trip.tripId)
                    .take(3)
                    .map { it.placeName }
                TripWithRoutes(trip, locations)
            }
        }
        .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())


    fun onTripDoubleClick(tripId: Long, title: String, onNavigateToEdit: (Long, String) -> Unit) {
        onNavigateToEdit(tripId, title)
    }


    fun addTrip(item: Trip, onResult: (Long) -> Unit) {
        viewModelScope.launch {
            val id = tripDao.insert(item)
            loadTrips()
            onResult(id)
        }
    }



    fun getTripById(tripId: Long) {
        viewModelScope.launch {
            val trip = tripDao.getByTripId(tripId)
            _currentTrip.value = trip
        }
    }


    fun loadTrips() {
        viewModelScope.launch {
            val items = tripDao.getAllTrips()
            _tripItems.postValue(items)
        }
    }

    fun updateTrip(item: Trip) {
        viewModelScope.launch {
            tripDao.update(item)
            loadTrips()
        }
    }

    fun deleteTrip(item: Trip) {
        viewModelScope.launch {
            tripDao.delete(item)
            loadTrips()
        }
    }

    // ---------- BUDGET ----------

    fun loadBudgets(tripId: Long) {
        viewModelScope.launch {
            val items = budgetDao.getByTripId(tripId)
            _budgetItems.postValue(items)
        }
    }

    fun addBudgetItem(budget: Budget) {
        viewModelScope.launch {
            budgetDao.insert(budget)
            loadBudgets(budget.tripId)
        }
    }

    fun deleteBudget(budget: Budget) {
        viewModelScope.launch {
            budgetDao.delete(budget)
            loadBudgets(budget.tripId)
        }
    }

    // ---------- TICKET ----------
    fun addTicketItem(item: Ticket) {
        viewModelScope.launch {
            ticketDao.insert(item)
            loadTickets(item.tripId)
        }
    }
    fun loadTickets(tripId: Long) {
        viewModelScope.launch {
            val items = ticketDao.getByTripId(tripId)
            _ticketItems.postValue(items)
        }
    }

    fun addTicket(item: Ticket) {
        viewModelScope.launch {
            ticketDao.insert(item)
            loadTickets(item.tripId)
        }
    }

    fun updateTicket(item: Ticket) {
        viewModelScope.launch {
            ticketDao.update(item)
            loadTickets(item.tripId)
        }
    }

    fun deleteTicket(item: Ticket) {
        viewModelScope.launch {
            ticketDao.delete(item)
            loadTickets(item.tripId)
        }
    }

    // ---------- PACKING LIST ----------
    fun addBaggageItem(item: PackingList) {
        viewModelScope.launch {
            packingListDao.insert(item)
            loadPackingListItems(item.tripId)
        }
    }
    fun loadPackingListItems(tripId: Long) {
        viewModelScope.launch {
            val items = packingListDao.getByTripId(tripId)
            _packingListItems.postValue(items)
        }
    }

    fun addPackingListItem(item: PackingList) {
        viewModelScope.launch {
            packingListDao.insert(item)
            loadPackingListItems(item.tripId)
        }
    }

    fun updatePackingListItem(item: PackingList) {
        viewModelScope.launch {
            packingListDao.update(item)
            loadPackingListItems(item.tripId)
        }
    }

    fun deletePackingListItem(item: PackingList) {
        viewModelScope.launch {
            packingListDao.delete(item)
            loadPackingListItems(item.tripId)
        }
    }

    // ---------- Hotel ----------
    fun addHotel(hotel: Hotel) {
        viewModelScope.launch {
            hotelDao.insert(hotel)
            loadHotels(hotel.tripId)
        }
    }

    fun deleteHotel(hotel: Hotel) {
        viewModelScope.launch {
            hotelDao.delete(hotel)
            loadHotels(hotel.tripId)
        }
    }

    fun loadHotels(tripId: Long) {
        viewModelScope.launch {
            val items = hotelDao.getHotelsForTrip(tripId)
            _hotelItems.postValue(items)
        }
    }

    fun updateHotel(hotel: Hotel) {
        viewModelScope.launch {
            hotelDao.update(hotel)
            loadHotels(hotel.tripId)
        }
    }

    // ---------- ROUTES ----------
    fun loadRoutes(tripId: Long) {
        viewModelScope.launch {
            val routes = routeDao.getRoutesForTrip(tripId)
            _routeItems.postValue(routes)
        }
    }

    fun addRoute(route: Route) {
        viewModelScope.launch {
            routeDao.insert(route)
            loadRoutes(route.tripId)
        }
    }

    fun updateRoute(route: Route) {
        viewModelScope.launch {
            routeDao.update(route)
            loadRoutes(route.tripId)
        }
    }

    fun deleteRoute(route: Route) {
        viewModelScope.launch {
            routeDao.delete(route)
            loadRoutes(route.tripId)
        }
    }


    fun moveRouteUp(index: Int, route: Route) {
        viewModelScope.launch {
            val routes = routeDao.getRoutesForTrip(route.tripId).toMutableList();
            if (index > 0) {
                Collections.swap(routes, index, index - 1)
                routeDao.clearAllRoutesForTrip(route.tripId)
                routeDao.insertAll(routes)
                loadRoutes(route.tripId)
            }
        }
    }

    fun moveRoute(fromIndex: Int, toIndex: Int) {
        val currentRoutes = routeItems.value?.toMutableList() ?: return
        if (fromIndex in currentRoutes.indices && toIndex in currentRoutes.indices) {
            val item = currentRoutes.removeAt(fromIndex)
            currentRoutes.add(toIndex, item)

            viewModelScope.launch {
                currentRoutes.forEachIndexed { index, route ->
                    if (route.orderIndex != index) {
                        viewModelScope.launch {
                            routeDao.update(route.copy(orderIndex = index))
                        }
                    }
                }
                _routeItems.postValue(currentRoutes)
            }
        }
    }


    private fun reorderRoutes(tripId: Long) {
        viewModelScope.launch {
            val routes = routeDao.getRoutesForTrip(tripId)
            val reordered = routes.mapIndexed { i, route -> route.copy(orderIndex = i) }
            routeDao.clearAllRoutesForTrip(tripId)
            routeDao.insertAll(reordered)
        }
    }


    fun selectPlace(route: Route) {
        _selectedPlace.value = route
    }

    fun clearSelectedPlace() {
        _selectedPlace.value = null
    }





    fun deleteNote(note: Note) {
        viewModelScope.launch {
            noteDao.delete(note)
            loadNotes(note.tripId)
        }
    }

    // ---------- NOTES ----------

    fun loadNotes(tripId: Long) {
        viewModelScope.launch {
            val items = noteDao.getByTripId(tripId)
            _noteItems.postValue(items)
        }
    }

    fun deleteNoteItem(note: Note) {
        viewModelScope.launch {
            noteDao.delete(note)
            loadNotes(note.tripId)
        }
    }

    fun addNoteItem(note: Note) {
        viewModelScope.launch {
            noteDao.insert(note)
            loadNotes(note.tripId)
        }
    }


}

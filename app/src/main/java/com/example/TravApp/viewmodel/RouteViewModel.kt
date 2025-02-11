package com.example.TravApp.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.TravApp.data.TravDatabase
import com.example.TravApp.data.Route
import com.example.TravApp.data.RouteRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class RouteViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: RouteRepository
    val allRoutes: LiveData<List<Route>>

    init {
        val routeDao = TravDatabase.getDatabase(application).routeDao()
        repository = RouteRepository(routeDao)
        allRoutes = repository.allRoutes
    }

    fun insert(route: Route) = viewModelScope.launch(Dispatchers.IO) {
        repository.insert(route)
    }

    fun update(route: Route) = viewModelScope.launch(Dispatchers.IO) {
        repository.update(route)
    }

    fun delete(route: Route) = viewModelScope.launch(Dispatchers.IO) {
        repository.delete(route)
    }
}

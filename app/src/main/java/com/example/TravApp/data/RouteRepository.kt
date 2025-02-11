package com.example.TravApp.data

import androidx.lifecycle.LiveData

class RouteRepository(private val routeDao: RouteDao) {

    val allRoutes: LiveData<List<Route>> = routeDao.getAllRoutes()

    suspend fun insert(route: Route) {
        routeDao.insert(route)
    }

    suspend fun update(route: Route) {
        routeDao.update(route)
    }

    suspend fun delete(route: Route) {
        routeDao.delete(route)
    }
}

package com.example.TravApp.data

import androidx.lifecycle.LiveData
import androidx.room.*
import kotlinx.coroutines.flow.Flow


@Dao
interface RouteDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(route: Route)

    @Update
    suspend fun update(route: Route)

    @Delete
    suspend fun delete(route: Route)

    @Query("SELECT * FROM route")
    fun getAllRoutes(): LiveData<List<Route>>

    @Query("SELECT * FROM route")
    fun getAllRoutes2(): Flow<List<Route>>

    @Query("SELECT * FROM route WHERE tripId = :tripId ORDER BY orderIndex ASC")
    suspend fun getRoutesForTrip(tripId: Long): List<Route>

    @Query("DELETE FROM route WHERE tripId = :tripId")
    suspend fun clearAllRoutesForTrip(tripId: Long)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(routes: List<Route>)

    @Query("DELETE FROM route")
    suspend fun deleteAllRoutes()
}



package com.example.TravApp.data

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface RouteDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(route: Route) // Вставка нового маршрута

    @Update
    suspend fun update(route: Route) // Обновление маршрута

    @Delete
    suspend fun delete(route: Route) // Удаление маршрута

    @Query("SELECT * FROM route WHERE id = :id")
    suspend fun getRouteById(id: Int): Route? // Получение маршрута по ID

    @Query("SELECT * FROM route")
    fun getAllRoutes(): LiveData<List<Route>> // Получение списка всех маршрутов

    @Query("DELETE FROM route")
    suspend fun deleteAllRoutes() // Очистка таблицы маршрутов
}

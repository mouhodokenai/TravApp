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

    @Query("SELECT * FROM routes WHERE id = :id")
    suspend fun getRouteById(id: Int): Route? // Получение маршрута по ID

    @Query("SELECT * FROM routes")
    fun getAllRoutes(): LiveData<List<Route>> // Получение списка всех маршрутов

    @Query("DELETE FROM routes")
    suspend fun deleteAllRoutes() // Очистка таблицы маршрутов
}

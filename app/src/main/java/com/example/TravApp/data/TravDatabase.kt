package com.example.TravApp.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
@Database(
    entities = [Route::class, Trip::class, Ticket::class, Budget::class, PackingList::class, Note::class],
    version = 1,
    exportSchema = false
)
abstract class TravDatabase : RoomDatabase() {

    abstract fun routeDao(): RouteDao
    abstract fun tripDao(): TripDao
    abstract fun ticketDao(): TicketDao
    abstract fun budgetDao(): BudgetDao
    abstract fun packingListDao(): PackingListDao
    abstract fun noteDao(): NoteDao

    companion object {
        @Volatile
        private var INSTANCE: TravDatabase? = null

        fun getDatabase(context: Context): TravDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    TravDatabase::class.java,
                    "travel_app_database"
                )
                    .fallbackToDestructiveMigration() // Удаляет старые данные при изменении схемы
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}

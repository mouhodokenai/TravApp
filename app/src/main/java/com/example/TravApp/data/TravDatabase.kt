package com.example.TravApp.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
@Database(
    entities = [Route::class, Trip::class, Ticket::class, Budget::class, PackingList::class, Note::class, Hotel::class],
    version = 2,
    exportSchema = true
)
abstract class TravDatabase : RoomDatabase() {

    abstract fun routeDao(): RouteDao
    abstract fun tripDao(): TripDao
    abstract fun ticketDao(): TicketDao
    abstract fun budgetDao(): BudgetDao
    abstract fun packingListDao(): PackingListDao
    abstract fun hotelDao(): HotelDao
    abstract fun noteDao(): NoteDao

    companion object {
        @Volatile
        private var INSTANCE: TravDatabase? = null

        fun getInstance(context: Context): TravDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    TravDatabase::class.java,
                    "travel_db"
                )
                .fallbackToDestructiveMigration()
                .build()
                INSTANCE = instance
                instance
            }
        }
    }
}

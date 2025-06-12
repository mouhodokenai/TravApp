package com.example.TravApp.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import java.util.UUID

@Entity(tableName = "tickets",
    foreignKeys = [
        ForeignKey(
            entity = Trip::class,
            parentColumns = ["trip_id"],
            childColumns = ["trip_id"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index(value = ["trip_id"])]
)
data class Ticket(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "ticket_id") val ticket_id: Int = 0,
    @ColumnInfo(name = "trip_id") val tripId: Long,
    @ColumnInfo(name = "transport_type") val transport_type: String,
    @ColumnInfo(name = "departure_city") val departure_city: String,
    @ColumnInfo(name = "arrival_city") val arrival_city: String,
    @ColumnInfo(name = "departure_time") val departure_time: String,
    @ColumnInfo(name = "arrival_time") val arrival_time: String,
    @ColumnInfo(name = "departure_date") val departure_date: String,
    @ColumnInfo(name = "arrival_date") val arrival_date: String,
    @ColumnInfo(name = "ticket_number") val ticket_number: String
)





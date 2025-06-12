package com.example.TravApp.screens

import android.app.Application
import android.content.Context
import android.os.Bundle
import android.widget.Toast
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.TravApp.data.Trip


import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import com.example.TravApp.data.TravViewModel
import java.text.SimpleDateFormat
import java.util.Locale

@Composable
fun PlanDashboard(
    viewModel: TravViewModel,
    onNavigateToNewTrip: () -> Unit,
    onNavigateToEditTrip: (Long, String) -> Unit,
    onNavigateBack: () -> Unit
) {
    val trips by viewModel.upcomingTrips.collectAsState()
    val context = LocalContext.current.applicationContext as Application
    // Для детекции двойного клика добавим вспомогательную логику:
    val lastClickTime = remember { mutableStateOf(0L) }
    val doubleClickTimeout = 300L // мс



    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .background(Color(0xFFFFF1D7)),
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Мои планы",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            LazyColumn(
                modifier = Modifier.weight(1f)
            ) {
                items(trips) { tripWithRoutes ->
                    PlanCard(
                        trip = tripWithRoutes.trip,
                        locations = tripWithRoutes.locations,
                        onDoubleClick = {
                            viewModel.onTripDoubleClick(
                                tripWithRoutes.trip.tripId,
                                tripWithRoutes.trip.title,
                                onNavigateToEditTrip
                            )
                        },
                        viewModel = viewModel,
                        context = context
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            AddButton(
                onClick = onNavigateToNewTrip,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

fun formatDateForDisplay(rawDate: String): String {
    val displayFormat = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault())
    val dateFormat = SimpleDateFormat("EEE MMM dd HH:mm:ss z yyyy", Locale.ENGLISH)
    return try {
        val parsedDate = dateFormat.parse(rawDate) // исходный формат
        if (parsedDate != null) {
            displayFormat.format(parsedDate) // формат для показа
        } else {
            "Ошибка даты"
        }
    } catch (e: Exception) {
        "Ошибка даты"
    }
}


@Composable
fun PlanCard(
    trip: Trip,
    locations: List<String>,
    onDoubleClick: () -> Unit,
    viewModel: TravViewModel,
    context: Context
) {
    var clickCount by remember { mutableStateOf(0) }
    var lastClickTimestamp by remember { mutableStateOf(0L) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable {
                val currentTime = System.currentTimeMillis()
                if (currentTime - lastClickTimestamp < 300) {
                    clickCount++
                } else {
                    clickCount = 1
                }
                lastClickTimestamp = currentTime
                if (clickCount == 2) {
                    onDoubleClick()
                    clickCount = 0
                }
            },
        colors = CardDefaults.cardColors(containerColor = Color(0xFFE2CDE2)),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(text = trip.title, fontSize = 20.sp, fontWeight = FontWeight.Bold)
                IconButton(onClick = {
                    viewModel.deleteTrip(trip)
                    Toast.makeText(context, "Удалено: ${trip.title}", Toast.LENGTH_SHORT).show()
                }) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "Удалить",
                        tint = Color(0xFFDDA0DD)
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            if (locations.isNotEmpty()) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(imageVector = Icons.Default.LocationOn, contentDescription = "Места")
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = locations.joinToString(", "),
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(imageVector = Icons.Default.DateRange, contentDescription = "Дата")
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "${formatDateForDisplay(trip.start_date)} – ${formatDateForDisplay(trip.end_date)}",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium
                )
            }
        }
    }
}


@Composable
fun AddButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .height(60.dp)
            .clickable { onClick() }
            .padding(4.dp),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(4.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF90CAF9))
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFAFDBF5)),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "Добавить",
                fontSize = 25.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
        }
    }
}





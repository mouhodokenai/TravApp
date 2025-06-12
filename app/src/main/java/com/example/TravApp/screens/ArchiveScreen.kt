package com.example.TravApp.screens

import android.app.Application
import android.os.Bundle
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.TravApp.data.TravViewModel
import com.example.TravApp.data.Trip



@Composable
fun ArchiveDashboard(
    viewModel: TravViewModel,
    onNavigateToNewTrip: () -> Unit,
    onNavigateToEditTrip: (Long, String) -> Unit,
    onNavigateBack: () -> Unit
) {
    val trips by viewModel.comingTrips.collectAsState()
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
                text = "Мой архив",
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




package com.example.TravApp.screens

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.TravApp.data.Trip

class ArchiveScreen : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ArchiveDashboard(
                onNavigateToNewTrip = { /* Навигация к экрану создания новой поездки */ },
                onNavigateBack = {}
            )
        }
    }
}

@Composable
fun ArchiveDashboard(
    onNavigateToNewTrip: () -> Unit,
    onNavigateBack: () -> Unit
) {
    val trips = listOf(
        Trip(title = "Черное море", start_date  = "10.07.2022", end_date = "20.05.2023", locations = listOf("Сочи", "Адлер")),
        Trip(title = "Калининград", start_date  = "01.07.2024", end_date = "10.07.2024", locations = listOf("Калининградская область")),
        Trip(title = "Дагестан", start_date  = "17.07.2021", end_date = "24.07.2021", locations = listOf("Дербент", "Каспийск"))
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
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
                items(trips) { trip ->
                    PlanCard(trip)
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



@Preview(showBackground = true)
@Composable
fun ArchiveDashboardV() {
    PlanDashboard(
        onNavigateToNewTrip = {},
        onNavigateBack = {})
}


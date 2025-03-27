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

class PlansScreen : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PlanDashboard(
                onNavigateToNewTrip = { /* Навигация к экрану создания новой поездки */ },
                onNavigateBack = {}
            )
        }
    }
}

@Composable
fun PlanDashboard(
    onNavigateToNewTrip: () -> Unit,
    onNavigateBack: () -> Unit
) {
    val trips = listOf(
        Trip(title = "Поездка в Париж", start_date  = "10.05.2025", end_date = "20.05.2025", locations = listOf("Париж", "Версаль")),
        Trip(title = "Горы Кавказа", start_date  = "01.06.2025", end_date = "10.06.2025", locations = listOf("Сочи", "Красная Поляна")),
        Trip(title = "К Настюше", start_date  = "хз когда", end_date = "надеюсь скоро", locations = listOf("Испания", "Италия")),
        Trip(title = "Север России", start_date  = "12.12.2025", end_date = "19.12.2025", locations = listOf("Мурманск", "Карелия")),
        Trip(title = "Азия", start_date  = "29.03.2026", end_date = "24.04.2026", locations = listOf("Вьетнам", "Тайланд", "Лаос"))
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

            // Центрируем заголовок
            Text(
                text = "Мои планы",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Позволяет LazyColumn занимать оставшееся место
            LazyColumn(
                modifier = Modifier.weight(1f) // Растягивает список, оставляя место для кнопки
            ) {
                items(trips) { trip ->
                    PlanCard(trip)
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Кнопка всегда внизу
            AddButton(
                onClick = onNavigateToNewTrip,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}


@Composable
fun AddButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier // Добавляем параметр модификатора
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
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text(text = "Добавить", fontSize = 25.sp, fontWeight = FontWeight.Bold, color = Color.White)
        }
    }
}


@Composable
fun PlanCard(trip: Trip) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
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
                Icon(imageVector = Icons.Default.Menu, contentDescription = "Название")
            }

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(imageVector = Icons.Default.LocationOn, contentDescription = "Места")
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = trip.locations.joinToString(", "),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium
                )
            }

            Spacer(modifier = Modifier.height(12.dp))
                
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(imageVector = Icons.Default.DateRange, contentDescription = "Дата")
                Spacer(modifier = Modifier.width(8.dp))
                Text(text = trip.start_date + " - " + trip.end_date, fontSize = 16.sp, fontWeight = FontWeight.Medium)
            }

        }
    }
}


@Preview(showBackground = true)
@Composable
fun PlansDashboardV() {
    PlanDashboard(
        onNavigateToNewTrip = {},
        onNavigateBack = {})
}


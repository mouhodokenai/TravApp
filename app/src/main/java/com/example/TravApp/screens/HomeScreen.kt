package com.example.TravApp.screens

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

class HomeScreen : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TravelDashboard(
                onNavigateToNewTrip = { /* Навигация к экрану создания новой поездки */ },
                onNavigateToPlans = { /* Навигация к планам */ },
                onNavigateToArchive = { /* Навигация к архиву */ }
            )
        }
    }
}

@Composable
fun TravelDashboard(
    onNavigateToNewTrip: () -> Unit,
    onNavigateToPlans: () -> Unit,
    onNavigateToArchive: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(60.dp))
        Text(
            text = "Добрый день!",
            fontSize = 40.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(30.dp))

        Text(
            text = "Мои достижения",
            fontSize = 30.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(30.dp))

        // Строка с круглыми карточками статистики
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            StatCard(label = "Города", value = 10)
            StatCard(label = "Страны", value = 3)
            StatCard(label = "Поездки", value = 7)
        }

        Spacer(modifier = Modifier.height(32.dp))

        // Карточка "Добавить"
        CardButton(text = "Добавить", height = 130, font = 30, onClick = onNavigateToNewTrip)
        Spacer(modifier = Modifier.height(25.dp))

        // Карточка "Планы"
        CardButton(text = "Планы", height = 130, font = 30, onClick = onNavigateToPlans)
        Spacer(modifier = Modifier.height(25.dp))

        // Карточка "Архив"
        CardButton(text = "Архив", height = 130, font = 30, onClick = onNavigateToArchive)
    }
}

// Круглая карточка для элемента статистики
@Composable
fun StatCard(label: String, value: Int) {
    Card(
        modifier = Modifier.size(100.dp), // Размер карточки
        shape = CircleShape,
        elevation = CardDefaults.cardElevation(4.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF90CAF9)) // Голубой фон
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(text = "$value", fontSize = 32.sp, fontWeight = FontWeight.Bold, color = Color.White)
            Text(text = label, fontSize = 20.sp, color = Color.White)
        }
    }
}

// Кнопка-карточка
@Composable
fun CardButton(
    text: String,
    height: Int,
    font: Int,
    onClick: () -> Unit,
    modifier: Modifier = Modifier) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(height.dp)
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
            Text(text = text, fontSize = font.sp, fontWeight = FontWeight.Bold, color = Color.White)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun TravelDashboardV() {
    TravelDashboard(
        onNavigateToNewTrip = {},
        onNavigateToPlans = {},
        onNavigateToArchive = {}
    )
}


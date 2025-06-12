package com.example.TravApp.screens

import android.app.Application
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.TravApp.data.Stats
import com.example.TravApp.data.TravViewModel
import com.example.TravApp.data.TravViewModelFactory

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
    val context = LocalContext.current
    val vmFactory =
        TravViewModelFactory(context.applicationContext as Application)
    val viewModel: TravViewModel = viewModel(factory = vmFactory)
    val stats by viewModel.getStats(context).collectAsState(initial = Stats(0, 0, 0))

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .background(Color(0xFFFFF1D7)),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(50.dp))
        Text(
            text = "Добрый день!",
            fontSize = 40.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF140800)
        )

        Spacer(modifier = Modifier.height(20.dp))

        Text(
            text = "Мои достижения",
            fontSize = 30.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(20.dp))

        // Строка с круглыми карточками статистики
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            StatCard(label = "Города", value = stats.cityCount)
            StatCard(label = "Страны", value = stats.countryCount)
            StatCard(label = "Поездки", value = stats.tripCount)
        }

        Spacer(modifier = Modifier.height(32.dp))
        
        CardButton(text = "Добавить", height = 100, font = 20, onClick = onNavigateToNewTrip)
        Spacer(modifier = Modifier.height(20.dp))

        CardButton(text = "Планы", height = 100, font = 20, onClick = onNavigateToPlans)
        Spacer(modifier = Modifier.height(20.dp))


        CardButton(text = "Архив", height = 100, font = 20, onClick = onNavigateToArchive)



    }
}

// Круглая карточка для элемента статистики
@Composable
fun StatCard(label: String, value: Int) {
    Card(
        modifier = Modifier.size(95.dp), // Размер карточки
        shape = CircleShape,
        elevation = CardDefaults.cardElevation(4.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF2AFC98)) // Голубой фон
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp)
                .background(Color(0xFF2AFC98)),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(text = "$value", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color.White)
            Text(text = label, fontSize = 16.sp, color = Color.White)
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
            modifier = Modifier.fillMaxSize().background(Color(0xFFAFDBF5)),
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


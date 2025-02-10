package com.example.testapp
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            TravelDashboard {
                // Переход к NewTripActivity при нажатии на кнопку
                val intent = Intent(this, NewTripActivity::class.java)
                startActivity(intent)
            }
        }
    }
}

@Composable
fun TravelDashboard(onAddTripClick: () -> Unit) {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Добрый день!", fontSize = 24.sp, fontWeight = FontWeight.Bold)

        Spacer(modifier = Modifier.height(16.dp))

        // Строка статистики (города, страны, поездки)
        Row(
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            StatItem(label = "города", value = 10)
            StatItem(label = "страны", value = 3)
            StatItem(label = "поездки", value = 7)
        }

        Spacer(modifier = Modifier.height(32.dp))

        // Кнопка для добавления
        Button(modifier = Modifier.fillMaxWidth(), onClick = onAddTripClick ) {
            Text(text = "добавить")
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Кнопки для действий (планы и архив)
        Row(
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Button(modifier = Modifier.weight(1f), onClick = { /* Обработка действия с планами */ }) {
                Text(text = "планы")
            }
            Button(modifier = Modifier.weight(1f), onClick = { /* Обработка действия с архивом */ }) {
                Text(text = "архив")
            }
        }
    }
}

@Composable
fun StatItem(label: String, value: Int) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(text = "$value", fontSize = 24.sp, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(4.dp))
        Text(text = label, fontSize = 14.sp, color = Color.Gray)
    }
}



package com.example.testapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

class TripDetailsActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Получаем название поездки из интента
        val tripName = intent.getStringExtra("trip_name") ?: "Без названия"
        val tripDepartureDate = intent.getStringExtra("trip_departure_date") ?: "нет"
        val tripReturnDate = intent.getStringExtra("trip_return_date") ?: "нет"
        setContent {
            TripDetailsScreen(tripName)
        }
    }
}

@Composable
fun TripDetailsScreen(tripName: String) {
    var route by remember { mutableStateOf("") }
    var transport by remember { mutableStateOf("") }
    var accommodation by remember { mutableStateOf("") }
    var budget by remember { mutableStateOf("") }
    var notes by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = tripName, fontSize = 24.sp, fontWeight = FontWeight.Bold)

        Spacer(modifier = Modifier.height(16.dp))

        InputField(value = route, onValueChange = { route = it }, label = "маршрут")
        InputField(value = transport, onValueChange = { transport = it }, label = "транспорт")
        InputField(value = accommodation, onValueChange = { accommodation = it }, label = "жилье")
        InputField(value = budget, onValueChange = { budget = it }, label = "бюджет")
        InputField(value = notes, onValueChange = { notes = it }, label = "заметки")

        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = { /* Сохранение данных */ }) {
            Text(text = "Сохранить")
        }
    }
}

@Composable
fun InputField(value: String, onValueChange: (String) -> Unit, label: String) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(text = label, fontSize = 16.sp, fontWeight = FontWeight.Medium)
        BasicTextField(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
                .height(40.dp)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewTripDetailsScreen() {
    TripDetailsScreen("название")
}

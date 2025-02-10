package com.example.testapp

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.abs

class NewTripActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            NewTripScreen()
        }
    }
}

@Composable
fun NewTripScreen() {
    var tripName by remember { mutableStateOf("") }
    var departureDate by remember { mutableStateOf<Date?>(null) }
    var returnDate by remember { mutableStateOf<Date?>(null) }
    val context = LocalContext.current
    val dateFormat = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault())

    val daysBetween = remember(departureDate, returnDate) {
        if (departureDate != null && returnDate != null) {
            val diff = returnDate!!.time - departureDate!!.time
            abs((diff / (1000 * 60 * 60 * 24)).toInt())
        } else {
            null
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Новая поездка", fontSize = 24.sp, fontWeight = FontWeight.Bold)

        Spacer(modifier = Modifier.height(16.dp))

        // Поле ввода названия поездки
        TextField(
            value = tripName,
            onValueChange = { tripName = it },
            placeholder = { Text("Название") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Выбор даты отправления
        DatePickerButton(
            label = "Дата туда",
            date = departureDate?.let { dateFormat.format(it) },
            onDateSelected = { departureDate = it }
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Выбор даты возвращения
        DatePickerButton(
            label = "Дата обратно",
            date = returnDate?.let { dateFormat.format(it) },
            onDateSelected = { returnDate = it }
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Вывод количества дней в поездке
        Text(text = daysBetween?.let { "$it дней" } ?: "", fontSize = 18.sp, fontWeight = FontWeight.Bold)

        Spacer(modifier = Modifier.height(32.dp))

        // Кнопка "Далее"
        Button(
            onClick = {
                val intent = Intent(context, TripDetailsActivity::class.java)
                intent.putExtra("trip_name", tripName)
                intent.putExtra("trip_departure_date", departureDate)
                intent.putExtra("trip_return_date", returnDate)
                context.startActivity(intent)
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = "Далее")
        }
    }
}

@Composable
fun DatePickerButton(label: String, date: String?, onDateSelected: (Date) -> Unit) {
    val context = LocalContext.current
    val calendar = Calendar.getInstance()

    Button(
        onClick = {
            DatePickerDialog(
                context,
                { _, year, month, dayOfMonth ->
                    calendar.set(year, month, dayOfMonth)
                    onDateSelected(calendar.time)
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
            ).show()
        },
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(text = date ?: label)
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewNewTripScreen() {
    NewTripScreen()
}

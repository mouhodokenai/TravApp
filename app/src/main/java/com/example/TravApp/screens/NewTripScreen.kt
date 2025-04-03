package com.example.TravApp.screens

import android.app.DatePickerDialog
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.testapp.R
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.abs

@Composable
fun NewTrip(
    onNavigateToDetails: (tripName: String, startDate: Date?, endDate: Date?) -> Unit, // Передаем данные
    onNavigateBack: () -> Unit) {

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

        Spacer(modifier = Modifier.height(60.dp))

        Text(
            text = "Новая поездка",
            fontSize = 40.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(25.dp))

        TextField(
            value = tripName,
            onValueChange = { tripName = it },
            placeholder = { Text("Название", fontSize = 25.sp) },
            textStyle = androidx.compose.ui.text.TextStyle(
                textAlign = TextAlign.Center
            ),
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.2f),
            singleLine = true
        )

        Spacer(modifier = Modifier.height(25.dp))

        Text(
            text = "дата туда",
            fontSize = 25.sp,
            fontWeight = FontWeight.Normal,
            textAlign = TextAlign.Left,
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(5.dp))

        // Выбор даты отправления
        DatePickerCard(
            text = departureDate?.let { dateFormat.format(it) } ?: "Выбрать дату",
            height = 60,
            font = 25
        ) { departureDate = it }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "дата обратно",
            fontSize = 25.sp,
            fontWeight = FontWeight.Normal,
            textAlign = TextAlign.Left,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(5.dp))
        // Выбор даты возвращения
        DatePickerCard(
            text = returnDate?.let { dateFormat.format(it) } ?: "Выбрать дату",
            height = 60,
            font = 25
        ) { returnDate = it }

        Spacer(modifier = Modifier.height(16.dp))

        // Вывод количества дней в поездке
        Text(text = daysBetween?.let { "$it дней" } ?: "", fontSize = 18.sp, fontWeight = FontWeight.Bold)

        Spacer(modifier = Modifier.height(35.dp))

        CardButton(
            text = "Далее",
            height = 60,
            font = 25,
            onClick = { onNavigateToDetails(tripName, departureDate, returnDate)  }
        )

    }
}

@Composable
fun DatePickerCard(
    text: String,
    height: Int,
    font: Int,
    onDateSelected: (Date) -> Unit
) {
    val context = LocalContext.current
    val calendar = Calendar.getInstance()

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(height.dp)
            .clickable {
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
            }
            .padding(4.dp),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(4.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF90CAF9))
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Image(
                painter = painterResource(id = R.drawable.calendar_date),
                contentDescription = "Выбор даты",
                modifier = Modifier.size(30.dp) // Размер иконки
            )
            Text(text = text, fontSize = font.sp, fontWeight = FontWeight.Bold, color = Color.White, textAlign = TextAlign.Center, modifier = Modifier.fillMaxWidth())

        }
    }
}


@Preview(showBackground = true)
@Composable
fun PreviewNewTripScreen() {
    NewTrip(
        onNavigateToDetails = { _, _, _ -> },
        onNavigateBack = {}
    )
}

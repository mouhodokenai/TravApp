package com.example.TravApp.screens

import android.app.DatePickerDialog
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.TravApp.data.TravViewModel
import com.example.TravApp.data.Trip
import com.example.testapp.R
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun NewTrip(
    viewModel: TravViewModel,
    onNavigateToDetails: (tripId: Long, tripName: String) -> Unit,
    onNavigateBack: () -> Unit
) {
    var tripName by remember { mutableStateOf("") }
    var departureDate by remember { mutableStateOf<Date?>(null) }
    var returnDate by remember { mutableStateOf<Date?>(null) }

    val context = LocalContext.current
    val dateFormat = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault())

    val isDateRangeValid = remember(departureDate, returnDate) {
        departureDate != null && returnDate != null && !returnDate!!.before(departureDate)
    }

    val daysBetween = remember(departureDate, returnDate) {
        if (isDateRangeValid) {
            val diff = returnDate!!.time - departureDate!!.time
            (diff / (1000 * 60 * 60 * 24)).toInt()
        } else null
    }

    val coroutineScope = rememberCoroutineScope()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .background(Color(0xFFFFF1D7)),
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
            placeholder = {
                Text("Название", fontSize = 25.sp, color = Color(0xFF101419))
            },
            textStyle = androidx.compose.ui.text.TextStyle(
                textAlign = TextAlign.Center,
                fontSize = 25.sp,
                color = Color(0xFF101419)
            ),
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.2f)
                .background(Color(0xFF2AFC98)),
            singleLine = true
        )

        Spacer(modifier = Modifier.height(25.dp))

        Text("дата туда", fontSize = 25.sp, modifier = Modifier.fillMaxWidth())
        Spacer(modifier = Modifier.height(5.dp))

        DatePickerCard(
            text = departureDate?.let { dateFormat.format(it) } ?: "Выбрать дату",
            height = 60,
            font = 25
        ) { departureDate = it }

        Spacer(modifier = Modifier.height(16.dp))

        Text("дата обратно", fontSize = 25.sp, modifier = Modifier.fillMaxWidth())
        Spacer(modifier = Modifier.height(5.dp))

        DatePickerCard(
            text = returnDate?.let { dateFormat.format(it) } ?: "Выбрать дату",
            height = 60,
            font = 25
        ) { returnDate = it }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = daysBetween?.let { "$it дней" } ?: "",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(35.dp))

        CardButton(
            text = "Далее",
            height = 60,
            font = 25,
            onClick = {
                when {
                    tripName.isBlank() -> {
                        Toast.makeText(context, "Введите название поездки", Toast.LENGTH_SHORT).show()
                    }
                    departureDate == null -> {
                        Toast.makeText(context, "Выберите дату отправления", Toast.LENGTH_SHORT).show()
                    }
                    returnDate == null -> {
                        Toast.makeText(context, "Выберите дату возвращения", Toast.LENGTH_SHORT).show()
                    }
                    returnDate!!.before(departureDate) -> {
                        Toast.makeText(context, "Дата возвращения не может быть раньше даты отправления", Toast.LENGTH_SHORT).show()
                    }
                    else -> {
                        coroutineScope.launch {
                            viewModel.addTrip(
                                Trip(
                                    title = tripName,
                                    start_date = departureDate!!.toString(),
                                    end_date = returnDate!!.toString()
                                )
                            ) { tripId ->
                                Log.d("MyApp", "tripId = $tripId")  // <-- вывод в лог
                                onNavigateToDetails(tripId, tripName)
                            }
                        }
                    }
                }
            }
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
                modifier = Modifier.size(30.dp)
            )
            Text(
                text = text,
                fontSize = font.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}




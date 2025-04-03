package com.example.TravApp.screens

import android.app.TimePickerDialog
import android.os.Bundle
import android.util.Log
import android.widget.TimePicker
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.room.parser.Section
import com.example.testapp.R
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign

import androidx.compose.ui.unit.dp
import java.util.Calendar



@Composable
fun TicketEntryScreen(
    onSelected: (String, String) -> Unit,
    onBack: () -> Unit
) {
    var shouldNavigateBack by remember { mutableStateOf(false) }

    if (shouldNavigateBack) {
        LaunchedEffect(Unit) {
            onBack()
        }
    }

    val context = LocalContext.current
    val calendar = Calendar.getInstance()
    val hour = calendar.get(Calendar.HOUR_OF_DAY)
    val minute = calendar.get(Calendar.MINUTE)

    var departureTime by remember { mutableStateOf("$hour:$minute") }
    var arrivalTime by remember { mutableStateOf("$hour:$minute") }

    val departurePicker = TimePickerDialog(
        context,
        { _: TimePicker, selectedHour: Int, selectedMinute: Int ->
            departureTime = "$selectedHour:$selectedMinute"
        }, hour, minute, true
    )

    val arrivalPicker = TimePickerDialog(
        context,
        { _: TimePicker, selectedHour: Int, selectedMinute: Int ->
            arrivalTime = "$selectedHour:$selectedMinute"
        }, hour, minute, true
    )

    var transportType by remember { mutableStateOf("Тип транспорта") }
    var departureCity by remember { mutableStateOf("") }
    var arrivalCity by remember { mutableStateOf("") }
    var ticketNumber by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Spacer(modifier = Modifier.height(10.dp))

        Text(text = "Добавление билета", fontSize = 24.sp, fontWeight = FontWeight.Bold)

        Spacer(modifier = Modifier.height(15.dp))

        DropdownMenuTransportType(selectedType = transportType, onSelectionChanged = { transportType = it })

        Spacer(modifier = Modifier.height(15.dp))

        OutlinedTextField(
            value = departureCity,
            onValueChange = { departureCity = it },
            placeholder = { Text("Город отправления",
                color = Color.Black,
                fontFamily = FontFamily.Default,
                fontSize = 15.sp) },

            modifier = Modifier.fillMaxWidth().height(50.dp)
        )

        Spacer(modifier = Modifier.height(15.dp))

        OutlinedTextField(
            value = arrivalCity,
            onValueChange = { arrivalCity = it },
            placeholder = { Text("Город прибытия",
                color = Color.Black,
                fontFamily = FontFamily.Default,
                fontSize = 15.sp) },
            modifier = Modifier.fillMaxWidth().height(50.dp)
        )

        Spacer(modifier = Modifier.height(15.dp))

        OutlinedButton(
            onClick = { departurePicker.show() },
            modifier = Modifier
                .fillMaxWidth()
                .height(55.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color.White),
            shape = RectangleShape
        ) {
            Text(
                text = "Время отправления: $departureTime",
                modifier = Modifier.fillMaxWidth(),
                color = Color.Black,
                textAlign = TextAlign.Start,
                fontWeight = FontWeight.Normal
            )
        }

        Spacer(modifier = Modifier.height(15.dp))

        OutlinedButton(
            onClick = { arrivalPicker.show() },
            modifier = Modifier.fillMaxWidth().height(55.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color.White),
            shape = RectangleShape
        ) {
            Text(
                text = "Время прибытия: $arrivalTime",
                modifier = Modifier.fillMaxWidth(),
                color = Color.Black,
                textAlign = TextAlign.Start,
                fontWeight = FontWeight.Normal
            )
        }

        Spacer(modifier = Modifier.height(15.dp))

        OutlinedTextField(
            value = ticketNumber,
            onValueChange = { ticketNumber = it },
            placeholder = { Text("Номер билета", color = Color.Black) },
            modifier = Modifier.fillMaxWidth().height(55.dp)
        )

        Spacer(modifier = Modifier.height(20.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Button(
                onClick = onBack,
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF90CAF9)
                )
            ) {
                Text("Назад")
            }

            Button(
                onClick = {
                    Log.d("Navigation", "Navigating to tickets")
                    onSelected(departureCity, arrivalCity)
                    shouldNavigateBack = true
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF90CAF9)
                )
            ) {
                Text("Сохранить")
            }
        }
        }

}

@Composable
fun DropdownMenuTransportType(
    selectedType: String?,
    onSelectionChanged: (String) -> Unit
) {
    val transportTypes = listOf("Самолет", "Поезд", "Автобус")
    var expanded by remember { mutableStateOf(false) }

    Column(modifier = Modifier.fillMaxWidth()) {
        OutlinedButton(
            onClick = { expanded = true },
            modifier = Modifier
                .fillMaxWidth()
                .height(55.dp),
            shape = RectangleShape
        ) {
            Text(
                text = selectedType ?: "Тип транспорта",
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Start,
                color = Color.Black,
                fontFamily = FontFamily.Default,
                fontSize = 15.sp,
                fontWeight = FontWeight.Normal
            )
        }

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier.fillMaxWidth()
        ) {
            transportTypes.forEach { type ->
                DropdownMenuItem(
                    text = { Text(type) },
                    onClick = {
                        onSelectionChanged(type)
                        expanded = false
                    }
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun TicketEntryScreenPreview() {
    TicketEntryScreen(
        onSelected = { _, _ -> },
        onBack = {}
    )
}

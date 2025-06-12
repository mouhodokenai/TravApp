package com.example.TravApp.screens

import android.app.TimePickerDialog
import android.os.Bundle
import android.util.Log
import android.widget.TimePicker
import android.widget.Toast
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

import com.example.testapp.R
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.material3.ExposedDropdownMenuDefaults.textFieldColors
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign

import androidx.compose.ui.unit.dp
import com.example.TravApp.data.Ticket
import com.example.TravApp.data.TravViewModel
import java.util.Calendar


// --- TicketEntryScreen.kt ---
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TicketEntryScreen(
    viewModel: TravViewModel,
    tripId: Long,
    onBack: () -> Unit,
) {
    var isAddScreen by remember { mutableStateOf(true) }
    var editingTicket by remember { mutableStateOf<Ticket?>(null) }

    val textColor = Color(0xFF000000)
    val background = Color(0xFFFFF1D7)
    val buttonColor = Color(0xFFAFDBF5)
    val AccentColor1 = Color(0xFFDDA0DD)
    val AccentColor2 = Color(0xFF2AFC98)
    val tickets by viewModel.ticketItems.observeAsState(emptyList())

    var transportType by remember { mutableStateOf("Тип транспорта") }
    var departureCity by remember { mutableStateOf("") }
    var arrivalCity by remember { mutableStateOf("") }
    val context = LocalContext.current
    val calendar = Calendar.getInstance()
    val hour = calendar.get(Calendar.HOUR_OF_DAY)
    val minute = calendar.get(Calendar.MINUTE)
    val year = calendar.get(Calendar.YEAR)
    val month = calendar.get(Calendar.MONTH)
    val day = calendar.get(Calendar.DAY_OF_MONTH)
    var departureTime by remember { mutableStateOf("") }
    var arrivalTime by remember { mutableStateOf("") }
    var departureDate by remember { mutableStateOf("") }
    var arrivalDate by remember { mutableStateOf("") }
    var ticketNumber by remember { mutableStateOf("") }

    val departureTimePicker = TimePickerDialog(context, { _, h, m ->
        departureTime = String.format("%02d:%02d", h, m)
    }, hour, minute, true)

    val arrivalTimePicker = TimePickerDialog(context, { _, h, m ->
        arrivalTime = String.format("%02d:%02d", h, m)
    }, hour, minute, true)

    val departureDatePicker = android.app.DatePickerDialog(context, { _, y, m, d ->
        departureDate = String.format("%02d.%02d.%d", d, m + 1, y)
    }, year, month, day)

    val arrivalDatePicker = android.app.DatePickerDialog(context, { _, y, m, d ->
        arrivalDate = String.format("%02d.%02d.%d", d, m + 1, y)
    }, year, month, day)

    fun clearForm() {
        editingTicket = null
        transportType = "Тип транспорта"
        departureCity = ""
        arrivalCity = ""
        departureTime = ""
        arrivalTime = ""
        departureDate = ""
        arrivalDate = ""
        ticketNumber = ""
    }

    LaunchedEffect(editingTicket) {
        editingTicket?.let {
            transportType = it.transport_type
            departureCity = it.departure_city
            arrivalCity = it.arrival_city
            departureTime = it.departure_time
            arrivalTime = it.arrival_time
            departureDate = it.departure_date
            arrivalDate = it.arrival_date
            ticketNumber = it.ticket_number
        }
    }

    Column(modifier = Modifier.fillMaxSize().background(background)) {
        TopAppBar(
            title = {
                Text(if (isAddScreen) "Добавление билета" else "Добавленные билеты", color = textColor)
            },
            navigationIcon = {
                IconButton(onClick = onBack) {
                    Icon(Icons.Default.ArrowBack, contentDescription = null, tint = textColor)
                }
            },
            actions = {
                TextButton(onClick = {
                    clearForm()
                    isAddScreen = !isAddScreen
                }) {
                    Text(if (isAddScreen) "Список" else "Добавить", color = textColor)
                }
            }
        )

        if (isAddScreen) {
            Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                DropdownMenuTransportType(transportType) { transportType = it }

                OutlinedTextField(
                    value = departureCity,
                    onValueChange = { departureCity = it },
                    label = { Text("Город отправления", color = textColor) },
                    modifier = Modifier.fillMaxWidth(),
                    colors = TextFieldDefaults.colors(
                        unfocusedContainerColor = Color.White,
                        focusedContainerColor = Color.White,
                    )
                )

                OutlinedTextField(
                    value = arrivalCity,
                    onValueChange = { arrivalCity = it },
                    label = { Text("Город прибытия", color = textColor) },
                    modifier = Modifier.fillMaxWidth(),
                    colors = TextFieldDefaults.colors(
                        unfocusedContainerColor = Color.White,
                        focusedContainerColor = Color.White,
                    )
                )

                OutlinedButton(onClick = { departureDatePicker.show() },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(55.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color.White),
                    shape = RectangleShape,
                    ) {
                    Text("Дата отправления: $departureDate", modifier = Modifier.fillMaxWidth(), textAlign = TextAlign.Start, color = Color.Black)
                }

                OutlinedButton(onClick = { departureTimePicker.show() },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(55.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color.White),
                    shape = RectangleShape,
                ) {
                    Text("Время отправления: $departureTime", modifier = Modifier.fillMaxWidth(), textAlign = TextAlign.Start, color = Color.Black)
                }

                OutlinedButton(onClick = { arrivalDatePicker.show() },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(55.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color.White),
                    shape = RectangleShape,
                ) {
                    Text("Дата прибытия: $arrivalDate", modifier = Modifier.fillMaxWidth(), textAlign = TextAlign.Start, color = Color.Black)
                }

                OutlinedButton(onClick = { arrivalTimePicker.show() },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(55.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color.White),
                    shape = RectangleShape,
                ) {
                    Text("Время прибытия: $arrivalTime", modifier = Modifier.fillMaxWidth(), textAlign = TextAlign.Start, color = Color.Black)
                }

                OutlinedTextField(
                    value = ticketNumber,
                    onValueChange = { ticketNumber = it },
                    label = { Text("Номер билета", color = textColor) },
                    modifier = Modifier.fillMaxWidth(),
                    colors = TextFieldDefaults.colors(
                        unfocusedContainerColor = Color.White,
                        focusedContainerColor = Color.White,
                    )
                )

                Button(
                    onClick = {
                        when {
                            departureCity.isBlank() || arrivalCity.isBlank() -> {
                                Toast.makeText(
                                    context,
                                    "Введите названия пунктов",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }

                            departureDate != null && arrivalDate != null && arrivalDate!! < departureDate -> {
                                Toast.makeText(
                                    context,
                                    "Дата выселения не может быть раньше даты заселения",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                            else -> {
                                if (editingTicket != null) {
                                    viewModel.updateTicket(
                                        editingTicket!!.copy(
                                            transport_type = transportType,
                                            departure_city = departureCity,
                                            arrival_city = arrivalCity,
                                            departure_time = departureTime,
                                            arrival_time = arrivalTime,
                                            departure_date = departureDate,
                                            arrival_date = arrivalDate,
                                            ticket_number = ticketNumber
                                        )
                                    )
                                    Toast.makeText(context, "Запись обновлена", Toast.LENGTH_SHORT).show()
                                } else {
                                    viewModel.addTicket(
                                        Ticket(
                                            tripId = tripId,
                                            transport_type = transportType,
                                            departure_city = departureCity,
                                            arrival_city = arrivalCity,
                                            departure_time = departureTime,
                                            arrival_time = arrivalTime,
                                            departure_date = departureDate,
                                            arrival_date = arrivalDate,
                                            ticket_number = ticketNumber
                                        )
                                    )
                                    Toast.makeText(context, "Запись сохранена", Toast.LENGTH_SHORT).show()
                                }
                            }
                        }
                        clearForm()
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = buttonColor)
                ) {
                    Text(if (editingTicket != null) "Обновить" else "Сохранить", color = textColor)
                }
            }
        } else {
            LazyColumn(modifier = Modifier.padding(16.dp)) {
                itemsIndexed(tickets) { _, ticket ->
                    Card(
                        modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth().padding(12.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text("Тип: ${ticket.transport_type}", color = textColor)
                                Text("Откуда: ${ticket.departure_city}", color = textColor)
                                Text("Куда: ${ticket.arrival_city}", color = textColor)
                                Text("Отправление: ${ticket.departure_date} ${ticket.departure_time}", color = textColor)
                                Text("Прибытие: ${ticket.arrival_date} ${ticket.arrival_time}", color = textColor)
                                Text("Билет №: ${ticket.ticket_number}", color = textColor)
                            }
                            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                IconButton(onClick = {
                                    editingTicket = ticket
                                    isAddScreen = true
                                }) {
                                    Icon(Icons.Default.Edit, contentDescription = "Редактировать", tint = AccentColor2)
                                }
                                IconButton(onClick = {
                                    viewModel.deleteTicket(ticket)
                                }) {
                                    Icon(Icons.Default.Delete, contentDescription = "Удалить", tint = AccentColor1)
                                }
                            }
                        }
                    }
                }
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
            modifier = Modifier.fillMaxWidth().height(55.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color.White),
            shape = RectangleShape
        ) {
            Text(
                text = selectedType ?: "Тип транспорта",
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Start,
                color = Color.Black,
                fontSize = 15.sp
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



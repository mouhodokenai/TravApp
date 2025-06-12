package com.example.TravApp.screens

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.TravApp.data.Hotel
import com.example.TravApp.data.TravViewModel
import java.util.Calendar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HotelEntryScreen(
    viewModel: TravViewModel,
    tripId: Long,
    onBack: () -> Unit
) {
    val hotels by viewModel.hotelItems.observeAsState(emptyList())
    val context = LocalContext.current

    var currentTab by remember { mutableStateOf(0) }

    var name by remember { mutableStateOf("") }
    var address by remember { mutableStateOf("") }
    var checkIn by remember { mutableStateOf("") }
    var checkInDateValue by remember { mutableStateOf<Calendar?>(null) }
    var checkOut by remember { mutableStateOf("") }
    var checkOutDateValue by remember { mutableStateOf<Calendar?>(null) }
    var checkInTime by remember { mutableStateOf("") }
    var checkOutTime by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf("") }

    var isEditing by remember { mutableStateOf(false) }
    var editingHotelId by remember { mutableStateOf<Int?>(null) }

    val calendar = Calendar.getInstance()

    fun clearFields() {
        name = ""
        address = ""
        checkIn = ""
        checkOut = ""
        checkInTime = ""
        checkOutTime = ""
        checkInDateValue = null
        checkOutDateValue = null
        errorMessage = ""
        isEditing = false
        editingHotelId = null
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFFFF1D7))
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = { onBack() }) {
                Icon(Icons.Default.ArrowBack, contentDescription = "Назад", tint = Color(0xFF101419))
            }
            Text(
                text = if (currentTab == 0) "Добавление жилья" else "Список отелей",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF101419)
            )
            Text(
                text = if (currentTab == 0) "Список" else "Добавить",
                color = Color(0xFF101419),
                modifier = Modifier.clickable { currentTab = 1 - currentTab }
            )
        }

        if (currentTab == 0) {
            // --- Поля ввода ---
            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                placeholder = { Text("Название", color = Color(0xFF101419)) },
                modifier = Modifier.fillMaxWidth().height(50.dp),
                colors = TextFieldDefaults.colors(
                    unfocusedContainerColor = Color.White,
                    focusedContainerColor = Color.White,
                )
            )

            OutlinedTextField(
                value = address,
                onValueChange = { address = it },
                placeholder = { Text("Адрес", color = Color(0xFF101419)) },
                modifier = Modifier.fillMaxWidth().height(50.dp),
                colors = TextFieldDefaults.colors(
                    unfocusedContainerColor = Color.White,
                    focusedContainerColor = Color.White,
                )
            )

            OutlinedButton(
                onClick = {
                    DatePickerDialog(
                        context,
                        { _, year, month, day ->
                            val c = Calendar.getInstance().apply {
                                set(year, month, day)
                            }
                            checkIn = String.format("%02d.%02d.%d", day, month + 1, year)
                            checkInDateValue = c
                        },
                        calendar.get(Calendar.YEAR),
                        calendar.get(Calendar.MONTH),
                        calendar.get(Calendar.DAY_OF_MONTH)
                    ).show()
                },
                modifier = Modifier.fillMaxWidth().height(50.dp),
                shape = RectangleShape,
                colors = ButtonDefaults.buttonColors(containerColor = Color.White)
            ) {
                Text("Дата заезда: $checkIn", modifier = Modifier.fillMaxWidth(), color = Color.Black)
            }

            OutlinedButton(
                onClick = {
                    TimePickerDialog(
                        context,
                        { _, hour, minute ->
                            checkInTime = String.format("%02d:%02d", hour, minute)
                        },
                        calendar.get(Calendar.HOUR_OF_DAY),
                        calendar.get(Calendar.MINUTE),
                        true
                    ).show()
                },
                modifier = Modifier.fillMaxWidth().height(50.dp),
                shape = RectangleShape,
                colors = ButtonDefaults.buttonColors(containerColor = Color.White)

            ) {
                Text("Время заезда: $checkInTime", modifier = Modifier.fillMaxWidth(), color = Color.Black)
            }

            OutlinedButton(
                onClick = {
                    DatePickerDialog(
                        context,
                        { _, year, month, day ->
                            val c = Calendar.getInstance().apply {
                                set(year, month, day)
                            }
                            checkOut = String.format("%02d.%02d.%d", day, month + 1, year)
                            checkOutDateValue = c
                        },
                        calendar.get(Calendar.YEAR),
                        calendar.get(Calendar.MONTH),
                        calendar.get(Calendar.DAY_OF_MONTH)
                    ).show()
                },
                modifier = Modifier.fillMaxWidth().height(50.dp),
                shape = RectangleShape,
                colors = ButtonDefaults.buttonColors(containerColor = Color.White)
            ) {
                Text("Дата отъезда: $checkOut", modifier = Modifier.fillMaxWidth(), color = Color.Black)
            }

            OutlinedButton(
                onClick = {
                    TimePickerDialog(
                        context,
                        { _, hour, minute ->
                            checkOutTime = String.format("%02d:%02d", hour, minute)
                        },
                        calendar.get(Calendar.HOUR_OF_DAY),
                        calendar.get(Calendar.MINUTE),
                        true
                    ).show()
                },
                modifier = Modifier.fillMaxWidth().height(50.dp),
                shape = RectangleShape,
                colors = ButtonDefaults.buttonColors(containerColor = Color.White)
            ) {
                Text("Время отъезда: $checkOutTime", modifier = Modifier.fillMaxWidth(), color = Color.Black)
            }

            if (errorMessage.isNotEmpty()) {
                Text(
                    text = errorMessage,
                    color = Color.Red,
                    modifier = Modifier.padding(top = 4.dp)
                )
            }

            Button(
                onClick = {
                    when {
                        name.isBlank() -> {
                            Toast.makeText(context, "Введите название отеля", Toast.LENGTH_SHORT).show()
                        }
                        checkInDateValue != null && checkOutDateValue != null && checkOutDateValue!! < checkInDateValue -> {
                            Toast.makeText(context, "Дата выезда не может быть раньше даты заезда", Toast.LENGTH_SHORT).show()
                        }
                        else -> {
                            val hotel = Hotel(
                                hotelId = editingHotelId ?: 0,
                                tripId = tripId,
                                name = name,
                                address = address,
                                checkInDate = checkIn,
                                checkOutDate = checkOut,
                                checkInTime = checkInTime,
                                checkOutTime = checkOutTime
                            )
                            if (isEditing) {
                                viewModel.updateHotel(hotel)
                                Toast.makeText(context, "Запись обновлена", Toast.LENGTH_SHORT).show()
                            } else {
                                viewModel.addHotel(hotel)
                                Toast.makeText(context, "Запись сохранена", Toast.LENGTH_SHORT).show()
                            }
                            clearFields()
                        }
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFAFDBF5))
            ) {
                Text(if (isEditing) "Обновить" else "Сохранить", color = Color(0xFF101419))
            }
        } else {
            LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                items(hotels.filter { it.tripId == tripId }) { hotel ->
                    Card(modifier = Modifier.fillMaxWidth()) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            Text(hotel.name, color = Color(0xFF101419), fontWeight = FontWeight.Bold)
                            hotel.address?.let { Text(it, color = Color.DarkGray, fontSize = 13.sp) }

                            Spacer(modifier = Modifier.height(4.dp))

                            Text("Заезд: ${hotel.checkInDate} ${hotel.checkInTime}", fontSize = 13.sp, color = Color.Black)
                            Text("Выезд: ${hotel.checkOutDate} ${hotel.checkOutTime}", fontSize = 13.sp, color = Color.Black)

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.End
                            ) {
                                IconButton(onClick = {
                                    name = hotel.name
                                    address = hotel.address ?: ""
                                    checkIn = hotel.checkInDate ?: ""
                                    checkOut = hotel.checkOutDate ?: ""
                                    checkInTime = hotel.checkInTime ?: ""
                                    checkOutTime = hotel.checkOutTime ?: ""
                                    isEditing = true
                                    editingHotelId = hotel.hotelId
                                    currentTab = 0
                                }) {
                                    Icon(Icons.Default.Edit, contentDescription = "Редактировать")
                                }

                                IconButton(onClick = {
                                    viewModel.deleteHotel(hotel)
                                }) {
                                    Icon(Icons.Default.Delete, contentDescription = "Удалить")
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}








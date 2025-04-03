package com.example.TravApp.screens

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

class NewTripDetailsScreen : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val tripName = intent.getStringExtra("trip_name") ?: "Без названия"
        val startDate = intent.getStringExtra("trip_departure_date") ?: "нет"
        val endDate = intent.getStringExtra("trip_return_date") ?: "нет"

        setContent {
            NewTripDetails(
                tripName = tripName,
                startDate = startDate,
                endDate = endDate,
                onNavigateBack = { finish() },
                onNavigateToRoute = { /* Обработчик навигации к маршруту */ },
                onNavigateToTickets = {},
                onNavigateToHotels = {},
                onNavigateToBudget = {},
                onNavigateToBaggage = {},
                onNavigateToNotes = {}
            )
        }
    }
}

@Composable
fun NewTripDetails(
    tripName: String,
    startDate: String,
    endDate: String,
    onNavigateBack: () -> Unit,
    onNavigateToRoute: () -> Unit,
    onNavigateToTickets: () -> Unit,
    onNavigateToHotels: () -> Unit,
    onNavigateToBudget: () -> Unit,
    onNavigateToBaggage: () -> Unit,
    onNavigateToNotes: () -> Unit
) {
    var showRouteScreen by remember { mutableStateOf(false) }
    var showTicketScreen by remember { mutableStateOf(false) }
    var showHotelScreen by remember { mutableStateOf(false) }
    var showBudgetScreen by remember { mutableStateOf(false) }
    var showBaggageScreen by remember { mutableStateOf(false) }
    var showNotesScreen by remember { mutableStateOf(false) }
    val places = remember { mutableStateListOf<String>() } // список мест
    val tickets = remember { mutableStateListOf<String>() } // список билетов
    val hotels = remember { mutableStateListOf<String>() } // список отелей
    val budgets = remember { mutableStateListOf<String>() } // список бюджета
    val clothes = remember { mutableStateListOf<String>() } // список багажа
    val notes = remember { mutableStateListOf<String>() } // список заметок


    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(16.dp))

        Text(text = tripName, fontSize = 24.sp, fontWeight = FontWeight.Bold)

        Spacer(modifier = Modifier.height(16.dp))

        RouteCard(
            places = places,
            onNavigateToRoute = { showRouteScreen = true }
        )

        TicketCard(
            tickets = tickets,
            onNavigateToTickets = { showTicketScreen = true}
        )

        HotelCard(
            hotels = hotels,
            onNavigateToHotels = { showHotelScreen = true}
        )

        BudgetCard(
            budgets = budgets,
            onNavigateToBudget = { showBudgetScreen = true}
        )

        BaggageCard(
            clothes = clothes,
            onNavigateToBaggage = { showBaggageScreen = true}
        )

        NoteCard(
            notes = notes,
            onNavigateToNotes = { showNotesScreen = true}
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = { /* Сохранение данных */ },
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF90CAF9)
            )
        ) {
            Text(text = "Сохранить")
        }

    }

    if (showRouteScreen) {
        RouteScreen(
            onPlaceSelected = { placeName, latLng ->
                places.add(placeName)
                showRouteScreen = false
            },
            onBack = { showRouteScreen = false }
        )
    }
    if (showTicketScreen) {
        TicketEntryScreen(
            onSelected = { departureCity, arrivalCity ->
                tickets.add(departureCity + " -> " + arrivalCity)
                showRouteScreen = false
            },
            onBack = { showTicketScreen = false }
        )
    }
    if (showHotelScreen) {
        HotelEntryScreen(
            onSave = { name ->
                hotels.add(name)
                showRouteScreen = false
            },
            onBack = { showTicketScreen = false }
        )
    }
}


@Composable
fun RouteCard(
    modifier: Modifier = Modifier,
    places: List<String>,
    onNavigateToRoute: () -> Unit
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(6.dp),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(text = "Маршрут", fontSize = 18.sp, fontWeight = FontWeight.Bold)
                Icon(imageVector = Icons.Default.Menu, contentDescription = "Маршрут")
            }

            Spacer(modifier = Modifier.height(6.dp))

            places.forEach { place ->
                Text(text = place, fontSize = 14.sp, modifier = Modifier.padding(vertical = 2.dp))
            }

            Spacer(modifier = Modifier.height(6.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onNavigateToRoute() },
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(imageVector = Icons.Default.Add, contentDescription = "Добавить")
                Spacer(modifier = Modifier.width(6.dp)) // Меньше отступ
                Text(text = "Добавить", fontSize = 14.sp, fontWeight = FontWeight.Medium)
            }
        }
    }
}


@Composable
fun TicketCard(
    modifier: Modifier = Modifier,
    tickets: List<String>,
    onNavigateToTickets: () -> Unit
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(6.dp),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(text = "Транспорт", fontSize = 18.sp, fontWeight = FontWeight.Bold)
                Icon(imageVector = Icons.Default.Menu, contentDescription = "Маршрут")
            }

            Spacer(modifier = Modifier.height(6.dp))

            tickets.forEach { ticket ->
                Text(text = ticket, fontSize = 14.sp, modifier = Modifier.padding(vertical = 2.dp))
            }

            Spacer(modifier = Modifier.height(6.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onNavigateToTickets() },
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(imageVector = Icons.Default.Add, contentDescription = "Добавить")
                Spacer(modifier = Modifier.width(6.dp))
                Text(text = "Добавить", fontSize = 14.sp, fontWeight = FontWeight.Medium)
            }
        }
    }
}


@Composable
fun HotelCard(
    modifier: Modifier = Modifier,
    hotels: List<String>,
    onNavigateToHotels: () -> Unit
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(6.dp),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(text = "Жилье", fontSize = 18.sp, fontWeight = FontWeight.Bold)
                Icon(imageVector = Icons.Default.Menu, contentDescription = "Жилье")
            }

            Spacer(modifier = Modifier.height(6.dp))

            hotels.forEach { hotel ->
                Text(text = hotel, fontSize = 14.sp, modifier = Modifier.padding(vertical = 2.dp))
            }

            Spacer(modifier = Modifier.height(6.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onNavigateToHotels() },
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(imageVector = Icons.Default.Add, contentDescription = "Добавить")
                Spacer(modifier = Modifier.width(6.dp))
                Text(text = "Добавить", fontSize = 14.sp, fontWeight = FontWeight.Medium)
            }
        }
    }
}


@Composable
fun BudgetCard(
    modifier: Modifier = Modifier,
    budgets: List<String>,
    onNavigateToBudget: () -> Unit
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(6.dp),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(text = "Бюджет", fontSize = 18.sp, fontWeight = FontWeight.Bold)
                Icon(imageVector = Icons.Default.Menu, contentDescription = "Бюджет")
            }

            Spacer(modifier = Modifier.height(6.dp))

            budgets.forEach { budget ->
                Text(text = budget, fontSize = 14.sp, modifier = Modifier.padding(vertical = 2.dp))
            }

            Spacer(modifier = Modifier.height(6.dp)) //   отступ

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onNavigateToBudget() },
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(imageVector = Icons.Default.Add, contentDescription = "Добавить")
                Spacer(modifier = Modifier.width(6.dp)) // Уменьшен отступ
                Text(text = "Добавить", fontSize = 14.sp, fontWeight = FontWeight.Medium)
            }
        }
    }
}


@Composable
fun BaggageCard(
    modifier: Modifier = Modifier,
    clothes: List<String>,
    onNavigateToBaggage: () -> Unit
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(6.dp),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(text = "Вещи", fontSize = 18.sp, fontWeight = FontWeight.Bold)
                Icon(imageVector = Icons.Default.Menu, contentDescription = "Багаж")
            }

            Spacer(modifier = Modifier.height(6.dp))

            clothes.forEach { clothe ->
                Text(text = clothe, fontSize = 14.sp, modifier = Modifier.padding(vertical = 2.dp))

            Spacer(modifier = Modifier.height(6.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onNavigateToBaggage() },
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(imageVector = Icons.Default.Add, contentDescription = "Добавить")
                Spacer(modifier = Modifier.width(6.dp))
                Text(text = "Добавить", fontSize = 14.sp, fontWeight = FontWeight.Medium) //  шрифт кнопки
            }
        }
    }
}


@Composable
fun NoteCard(
    modifier: Modifier = Modifier,
    notes: List<String>,
    onNavigateToNotes: () -> Unit
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(6.dp),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(text = "Заметки", fontSize = 18.sp, fontWeight = FontWeight.Bold)
                Icon(imageVector = Icons.Default.Menu, contentDescription = "Заметки")
            }

            Spacer(modifier = Modifier.height(6.dp))

            notes.forEach { note ->
                Text(text = note, fontSize = 14.sp, modifier = Modifier.padding(vertical = 2.dp))
            }

            Spacer(modifier = Modifier.height(6.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onNavigateToNotes() },
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(imageVector = Icons.Default.Add, contentDescription = "Добавить")
                Spacer(modifier = Modifier.width(6.dp))
                Text(text = "Добавить", fontSize = 14.sp, fontWeight = FontWeight.Medium)
            }
        }
    }
}


@Composable
fun ExpandableSection(title: String) {
    var expanded by remember { mutableStateOf(false) }
    var text by remember { mutableStateOf("") }

    Column(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { expanded = !expanded }
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = title, fontSize = 18.sp, fontWeight = FontWeight.Medium)
            Spacer(modifier = Modifier.weight(1f))
        }

        AnimatedVisibility(visible = expanded) {
            OutlinedTextField(
                value = text,
                onValueChange = { text = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                placeholder = { Text("Введите данные...") }
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun NewTripDetailsPreview() {
    NewTripDetails(
        tripName = "Путешествие в Париж",
        startDate = "10.06.2025",
        endDate = "20.06.2025",
        onNavigateBack = {},
        onNavigateToRoute = {},
        onNavigateToTickets = {},
        onNavigateToHotels = {},
        onNavigateToBudget = {},
        onNavigateToBaggage = {},
        onNavigateToNotes = {}
    )
}





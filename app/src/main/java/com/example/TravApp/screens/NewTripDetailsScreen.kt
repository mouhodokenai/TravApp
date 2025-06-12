package com.example.TravApp.screens

import android.app.Application
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.TravApp.data.TravViewModel
import com.example.TravApp.data.TravViewModelFactory
import com.example.testapp.R


@Composable
fun NewTripDetails(
    title: String,
    tripId: Long,
    onNavigateBack: () -> Unit,
    onNavigateHome: () -> Unit
) {

    val context = LocalContext.current.applicationContext as Application
        val viewModel: TravViewModel = viewModel(
        factory = TravViewModelFactory(context)
    )

    var showRouteScreen by remember { mutableStateOf(false) }
    var showTicketScreen by remember { mutableStateOf(false) }
    var showHotelScreen by remember { mutableStateOf(false) }
    var showBudgetScreen by remember { mutableStateOf(false) }
    var showBaggageScreen by remember { mutableStateOf(false) }
    var showNotesScreen by remember { mutableStateOf(false) }
    val places = remember { mutableStateListOf<String>() } // список мест *
    val tickets = remember { mutableStateListOf<String>() } // список билетов *
    val hotels = remember { mutableStateListOf<String>() }
    val budgets = remember { mutableStateListOf<String>() } // список бюджета *
    val clothes = remember { mutableStateListOf<String>() } // список багажа *
    val notes = remember { mutableStateListOf<String>() } // список заметок


    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .background(Color(0xFFFFF1D7)),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(16.dp))


            Text(
                text = title,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
            )


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
            items = hotels,
            onNavigateToHotels = { showHotelScreen = true }
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
            onClick = {onNavigateHome()},
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFFAFDBF5)
            )
        ) {
            Text(text = "Сохранить")
        }

    }

    if (showRouteScreen) {
        RouteScreen(
            viewModel = viewModel,
            tripId = tripId,
            onBack = { showRouteScreen = false }
        )
    }
    if (showTicketScreen) {
        TicketEntryScreen(
            viewModel = viewModel,
            tripId = tripId,
            onBack = { showTicketScreen = false }
        )
    }

    if (showHotelScreen) {
        HotelEntryScreen(
            viewModel = viewModel,
            tripId = tripId,
            onBack = { showHotelScreen = false }
        )
    }
    if (showBudgetScreen) {
        BudgetEntryScreen(
            viewModel = viewModel,
            tripId = tripId,
            onBack = { showBudgetScreen = false }
        )
    }
    if (showNotesScreen) {
        NoteScreen(
            viewModel = viewModel,
            tripId = tripId,
            onBack = { showNotesScreen = false }
        )
    }

    if (showBaggageScreen) {
        BaggageEntryScreen(
            viewModel = viewModel,
            tripId = tripId,
            onBack = { showBaggageScreen = false }
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
        elevation = CardDefaults.cardElevation(2.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFFE2CDE2)
        )
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(text = "Маршрут", fontSize = 18.sp, fontWeight = FontWeight.Bold)
                Icon(
                    painter = painterResource(id = R.drawable.route),
                    contentDescription = "Маршрут",
                    modifier = Modifier.size(35.dp)
                )
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
        elevation = CardDefaults.cardElevation(2.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFFE2CDE2)
        )
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(text = "Транспорт", fontSize = 18.sp, fontWeight = FontWeight.Bold)
                Icon(
                    painter = painterResource(id = R.drawable.airplane),
                    contentDescription = "Самолет",
                    modifier = Modifier.size(35.dp)
                )
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
    items: List<String>,
    onNavigateToHotels: () -> Unit
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(6.dp),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(2.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFFE2CDE2)
        )
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(text = "Жилье", fontSize = 18.sp, fontWeight = FontWeight.Bold)
                Icon(
                    painter = painterResource(id = R.drawable.hotel),
                    contentDescription = "Отель",
                    modifier = Modifier.size(35.dp)
                )
            }

            Spacer(modifier = Modifier.height(6.dp))

            items.forEach { hotel ->
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
        elevation = CardDefaults.cardElevation(2.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFFE2CDE2)
        )
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(text = "Бюджет", fontSize = 18.sp, fontWeight = FontWeight.Bold)
                Icon(
                    painter = painterResource(id = R.drawable.wallet),
                    contentDescription = "Бюджет",
                    modifier = Modifier.size(35.dp)
                )
            }

            Spacer(modifier = Modifier.height(6.dp))

            budgets.forEach { budget ->
                Text(text = budget, fontSize = 14.sp, modifier = Modifier.padding(vertical = 2.dp))
            }

            Spacer(modifier = Modifier.height(6.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onNavigateToBudget() },
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
        elevation = CardDefaults.cardElevation(2.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFFE2CDE2)
        )
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(text = "Заметки", fontSize = 18.sp, fontWeight = FontWeight.Bold)
                Icon(
                    painter = painterResource(id = R.drawable.notes),
                    contentDescription = "Заметки",
                    modifier = Modifier.size(35.dp)
                )
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
        elevation = CardDefaults.cardElevation(2.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFFE2CDE2)
        )
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(text = "Вещи", fontSize = 18.sp, fontWeight = FontWeight.Bold)
                Icon(
                    painter = painterResource(id = R.drawable.suitcase),
                    contentDescription = "Багаж",
                    modifier = Modifier.size(35.dp)
                )
            }

            Spacer(modifier = Modifier.height(6.dp))

            clothes.forEach { clothe ->
                Text(text = clothe, fontSize = 14.sp, modifier = Modifier.padding(vertical = 2.dp))
            }
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













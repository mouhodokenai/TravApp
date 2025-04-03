package com.example.TravApp.screens

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.TravApp.data.Route
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.*
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.AutocompletePrediction
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.api.net.FetchPlaceRequest
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsRequest
import com.google.android.libraries.places.api.net.PlacesClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import java.util.UUID
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.ui.tooling.preview.Preview


@Composable
fun RouteScreen(
    onPlaceSelected: (String, LatLng) -> Unit, // Добавляем координаты
    onBack: () -> Unit
) {
    var query by remember { mutableStateOf("") }
    var searchResults by remember { mutableStateOf(listOf<Route>()) }
    var isLoading by remember { mutableStateOf(false) }
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    val tripId = UUID.randomUUID()

    val selectedPlace = remember { mutableStateOf<LatLng?>(null) } // Храним выбранное место

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onBack) {
                Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Назад")
            }
            Text("Поиск мест", fontSize = 20.sp, fontWeight = FontWeight.Bold)
        }

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = query,
            onValueChange = { newQuery ->
                query = newQuery
                coroutineScope.launch {
                    isLoading = true
                    searchResults = searchPlaces(newQuery, tripId, context)
                    isLoading = false
                }
            },
            modifier = Modifier.fillMaxWidth(),
            label = { Text("Введите место") },
            trailingIcon = {
                if (isLoading) {
                    CircularProgressIndicator(modifier = Modifier.size(24.dp), strokeWidth = 2.dp)
                } else {
                    IconButton(onClick = {
                        coroutineScope.launch {
                            isLoading = true
                            searchResults = searchPlaces(query.trim(), tripId, context)
                            isLoading = false
                        }
                    }) {
                        Icon(imageVector = Icons.Default.Search, contentDescription = "Искать")
                    }
                }
            }
        )

        Spacer(modifier = Modifier.height(16.dp))

        val cameraPositionState = rememberCameraPositionState {
            position = CameraPosition.fromLatLngZoom(LatLng(55.751244, 37.618423), 10f) // Москва
        }

        Box(modifier = Modifier.fillMaxSize()) {
            GoogleMap(
                modifier = Modifier.matchParentSize(),
                cameraPositionState = cameraPositionState,
                onMapClick = { latLng ->
                    selectedPlace.value = latLng
                }
            ) {
                selectedPlace.value?.let { place ->
                    Marker(
                        state = rememberMarkerState(position = place),
                        title = "Выбранное место"
                    )
                }

                searchResults.forEach { place ->
                    Marker(
                        state = rememberMarkerState(position = LatLng(place.latitude, place.longitude)),
                        title = place.placeName
                    )
                }
            }

            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White.copy(alpha = 0.9f))
                    .padding(8.dp)
            ) {
                items(searchResults) { place ->
                    PlaceItem(placeName = place.placeName) {
                        onPlaceSelected(place.placeName, LatLng(place.latitude, place.longitude))
                        cameraPositionState.position =
                            CameraPosition.fromLatLngZoom(LatLng(place.latitude, place.longitude), 15f)
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        selectedPlace.value?.let { place ->
            Button(
                onClick = {
                    onPlaceSelected("Выбранное место", place) // Передаем название и координаты
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Добавить в маршрут")
            }
        }
    }
}


@Composable
fun PlaceItem(placeName: String, onSelect: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
            .clickable { onSelect() },
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Text(
            text = placeName,
            fontSize = 18.sp,
            modifier = Modifier.padding(16.dp)
        )
    }
}

suspend fun searchPlaces(query: String, tripId: UUID, context: Context): List<Route> {
    return withContext(Dispatchers.IO) { // Переключаем контекст
        suspendCoroutine { continuation ->
            val placesClient = Places.createClient(context)
            val request = FindAutocompletePredictionsRequest.builder()
                .setQuery(query)
                .build()

            placesClient.findAutocompletePredictions(request)
                .addOnSuccessListener { response ->
                    val places = response.autocompletePredictions.map { prediction ->
                        Route(
                            tripId = tripId,
                            placeName = prediction.getPrimaryText(null).toString(),
                            latitude = 0.0,
                            longitude = 0.0,
                            arrivalTime = "",
                            departureTime = "",
                            orderIndex = 0
                        )
                    }
                    continuation.resume(places)
                }
                .addOnFailureListener { exception ->
                    Log.e("PlacesAPI", "Ошибка запроса: ${exception.message}")
                    continuation.resume(emptyList())
                }
        }
    }
}



suspend fun getPlaceDetails(placeId: String, context: Context): LatLng? {
    val placesClient = Places.createClient(context)
    val request = FetchPlaceRequest.builder(placeId, listOf(Place.Field.LAT_LNG)).build()

    return suspendCoroutine { continuation ->
        placesClient.fetchPlace(request)
            .addOnSuccessListener { response ->
                continuation.resume(response.place.latLng) // Возвращаем координаты
            }
            .addOnFailureListener {
                continuation.resume(null)
            }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewRouteScreen() {
    RouteScreen(
        onPlaceSelected = TODO(),
        onBack = TODO()
    )
}
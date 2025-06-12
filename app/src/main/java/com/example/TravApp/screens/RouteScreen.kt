package com.example.TravApp.screens

import android.content.Context
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.TravApp.data.Route
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.*
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.api.net.FetchPlaceRequest
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsRequest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.livedata.observeAsState
import com.example.TravApp.data.TravViewModel
import com.google.android.gms.tasks.Tasks
import org.burnoutcrew.reorderable.ReorderableItem
import org.burnoutcrew.reorderable.detectReorderAfterLongPress
import org.burnoutcrew.reorderable.rememberReorderableLazyListState
import org.burnoutcrew.reorderable.reorderable
import java.util.concurrent.TimeUnit


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RouteScreen(
    viewModel: TravViewModel,
    tripId: Long,
    onBack: () -> Unit
) {
    val BackgroundColor = Color(0xFFFFF1D7)
    val TextColor = Color(0xFF000000)
    val ButtonColor = Color(0xFFAFDBF5)
    val AccentColor1 = Color(0xFFDDA0DD)
    val AccentColor2 = Color(0xFF2AFC98)

    var isSearchScreen by remember { mutableStateOf(true) }
    var query by remember { mutableStateOf("") }
    var searchResults by remember { mutableStateOf(listOf<Route>()) }
    var selectedPlace by remember { mutableStateOf<Route?>(null) }
    var isLoading by remember { mutableStateOf(false) }
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    val savedRoutes by viewModel.routeItems.observeAsState(initial = emptyList())
    val dragDropState = rememberReorderableLazyListState(onMove = { from, to ->
        viewModel.moveRoute(from.index, to.index)
    })

    LaunchedEffect(tripId) {
        viewModel.loadRoutes(tripId)
    }

    Column(modifier = Modifier.fillMaxSize().background(BackgroundColor)) {
        TopAppBar(
            title = {
                Text(
                    if (isSearchScreen) "Поиск мест" else "Сохранённые места",
                    color = TextColor
                )
            },
            navigationIcon = {
                IconButton(onClick = onBack) {
                    Icon(Icons.Default.ArrowBack, contentDescription = "Назад", tint = TextColor)
                }
            },
            actions = {
                TextButton(onClick = { isSearchScreen = !isSearchScreen }) {
                    Text(if (isSearchScreen) "Список" else "Поиск", color = TextColor)
                }
            }
        )

        if (isSearchScreen) {
            Column(modifier = Modifier.padding(16.dp)) {
                OutlinedTextField(
                    value = query,
                    onValueChange = { newQuery ->
                        query = newQuery
                        selectedPlace = null
                        if (query.length >= 2) {
                            coroutineScope.launch {
                                isLoading = true
                                searchResults = searchPlaces(query.trim(), tripId, context)
                                isLoading = false
                            }
                        } else {
                            searchResults = emptyList()
                        }
                    },
                    label = { Text("Введите место", color = TextColor) },
                    modifier = Modifier.fillMaxWidth(),
                    colors = TextFieldDefaults.colors(
                        unfocusedContainerColor = Color.White,
                        focusedContainerColor = Color.White,
                    ),
                    trailingIcon = {
                        if (isLoading) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(20.dp),
                                strokeWidth = 2.dp,
                                color = AccentColor2
                            )
                        } else {
                            Icon(Icons.Default.Search, contentDescription = null, tint = TextColor)
                        }
                    }
                )

                Spacer(modifier = Modifier.height(8.dp))

                if (selectedPlace == null) {
                    LazyColumn {
                        items(searchResults) { place ->
                            PlaceItem(place.placeName, TextColor) {
                                selectedPlace = place
                                query = place.placeName
                                searchResults = emptyList()
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                selectedPlace?.let { place ->
                    val cameraPositionState = rememberCameraPositionState {
                        position = CameraPosition.fromLatLngZoom(
                            LatLng(place.latitude, place.longitude), 14f
                        )
                    }

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(250.dp)
                    ) {
                        GoogleMap(
                            modifier = Modifier.fillMaxSize(),
                            cameraPositionState = cameraPositionState
                        ) {
                            Marker(
                                state = rememberMarkerState(
                                    position = LatLng(place.latitude, place.longitude)
                                ),
                                title = place.placeName
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Button(
                            onClick = {
                                viewModel.addRoute(place)
                                selectedPlace = null
                                query = ""
                                searchResults = emptyList()
                                isSearchScreen = false
                            },
                            modifier = Modifier.weight(1f),
                            colors = ButtonDefaults.buttonColors(containerColor = ButtonColor)
                        ) {
                            Text("Добавить", color = TextColor)
                        }

                        Spacer(modifier = Modifier.width(8.dp))

                        Button(
                            onClick = {
                                selectedPlace = null
                                query = ""
                                searchResults = emptyList()
                            },
                            modifier = Modifier.weight(1f),
                            colors = ButtonDefaults.buttonColors(containerColor = AccentColor1)
                        ) {
                            Text("Очистить", color = TextColor)
                        }
                    }
                }
            }
        } else {
            Column(modifier = Modifier.fillMaxSize()) {
                LazyColumn(
                    modifier = Modifier
                        .weight(1f)
                        .padding(8.dp)
                        .reorderable(dragDropState)
                        .detectReorderAfterLongPress(dragDropState),
                    state = dragDropState.listState
                ) {
                    itemsIndexed(savedRoutes, key = { _, item -> item.routeId }) { index, place ->
                        ReorderableItem(dragDropState, key = place.routeId) { isDragging ->
                            val elevation = animateDpAsState(if (isDragging) 8.dp else 2.dp)
                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 4.dp),
                                elevation = CardDefaults.cardElevation(elevation.value),
                                colors = CardDefaults.cardColors(containerColor = Color.White)
                            ) {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(8.dp),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = "${index + 1}. ${place.placeName}",
                                        modifier = Modifier.weight(1f),
                                        fontWeight = FontWeight.Medium,
                                        color = TextColor
                                    )
                                    Row {
                                        Icon(
                                            imageVector = Icons.Default.Menu,
                                            contentDescription = "Перетащить",
                                            modifier = Modifier.padding(end = 8.dp),
                                            tint = AccentColor2
                                        )

                                        IconButton(onClick = {
                                            viewModel.deleteRoute(place)
                                        }) {
                                            Icon(
                                                imageVector = Icons.Default.Delete,
                                                contentDescription = "Delete",
                                                tint = AccentColor1
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }

                GoogleMap(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight(),
                    cameraPositionState = rememberCameraPositionState {
                        position = CameraPosition.fromLatLngZoom(
                            LatLng(savedRoutes.firstOrNull()?.latitude ?: 0.0, savedRoutes.firstOrNull()?.longitude ?: 0.0), 5f
                        )
                    }
                ) {
                    savedRoutes.forEach {
                        Marker(
                            state = MarkerState(position = LatLng(it.latitude, it.longitude)),
                            title = it.placeName
                        )
                    }
                    if (savedRoutes.size > 1) {
                        Polyline(points = savedRoutes.map { LatLng(it.latitude, it.longitude) })
                    }
                }
            }
        }
    }
}

@Composable
fun PlaceItem(placeName: String, textColor: Color, onSelect: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
            .clickable(onClick = onSelect),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Text(
            text = placeName,
            fontSize = 18.sp,
            modifier = Modifier.padding(16.dp),
            color = textColor
        )
    }
}
suspend fun searchPlaces(query: String, tripId: Long, context: Context): List<Route> = withContext(Dispatchers.IO) {
    val placesClient = Places.createClient(context)

    val request = FindAutocompletePredictionsRequest.builder()
        .setQuery(query)
        .build()

    val resultList = mutableListOf<Route>()

    try {
        val response = Tasks.await(
            placesClient.findAutocompletePredictions(request),
            5,
            TimeUnit.SECONDS
        )

        response.autocompletePredictions.forEach { prediction ->
            val placeId = prediction.placeId

            val placeRequest = FetchPlaceRequest.builder(
                placeId,
                listOf(
                    Place.Field.ID,
                    Place.Field.NAME,
                    Place.Field.LAT_LNG
                )
            ).build()

            val placeResponse = Tasks.await(
                placesClient.fetchPlace(placeRequest),
                5,
                TimeUnit.SECONDS
            )

            val place = placeResponse.place
            val latLng = place.latLng

            if (latLng != null) {
                resultList.add(
                    Route(
                        tripId = tripId,
                        placeName = place.name ?: "Неизвестное место",
                        latitude = latLng.latitude,
                        longitude = latLng.longitude
                    )
                )
            }
        }
    } catch (e: Exception) {
        e.printStackTrace()
    }

    return@withContext resultList
}






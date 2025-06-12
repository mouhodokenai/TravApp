package com.example.TravApp.screens

import android.app.Application
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.material3.Text
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.TravApp.data.PackingList
import com.example.TravApp.data.TravViewModel

@Composable
fun BaggageEntryScreen(
    viewModel: TravViewModel,
    tripId: Long,
    onBack: () -> Unit,
) {
    val baggageItems by viewModel.packingListItems.observeAsState(emptyList())
    var newItem by remember { mutableStateOf("") }
    var shouldNavigateBack by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }
    val context = LocalContext.current.applicationContext as Application

    val AccentColor1 = Color(0xFFDDA0DD)

    if (shouldNavigateBack) {
        LaunchedEffect(Unit) {
            onBack()
        }
    }

    LaunchedEffect(Unit) {
        viewModel.loadPackingListItems(tripId)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .background(Color(0xFFFFF1D7)),
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = { onBack() }) {
                Icon(
                    Icons.Default.ArrowBack,
                    contentDescription = "Назад",
                    tint = Color(0xFF101419)
                )
            }
            Text("Багаж", fontSize = 24.sp, fontWeight = FontWeight.Bold)
        }
            Spacer(modifier = Modifier.height(16.dp))

            LazyColumn(modifier = Modifier.weight(1f)) {
                items(baggageItems) { item ->
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp)
                    ) {
                        Checkbox(
                            checked = item.is_packed,
                            onCheckedChange = {
                                viewModel.updatePackingListItem(item.copy(is_packed = !item.is_packed))
                            }
                        )
                        Text(
                            text = item.name,
                            modifier = Modifier.weight(1f)
                        )
                        IconButton(onClick = {
                            viewModel.deletePackingListItem(item)
                            Toast.makeText(context, "Удалено: ${item.name}", Toast.LENGTH_SHORT).show()
                        }) {
                            Icon(
                                imageVector = Icons.Default.Delete,
                                contentDescription = "Удалить",
                                tint = AccentColor1
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                OutlinedTextField(
                    value = newItem,
                    onValueChange = { newItem = it },
                    label = { Text("Добавить вещь") },
                    modifier = Modifier.weight(1f)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Button(
                    onClick = {
                        if (newItem.isBlank()) {
                            errorMessage = "Введите название вещи"
                            Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show()
                        } else {
                            viewModel.addPackingListItem(
                                PackingList(tripId = tripId, name = newItem, is_packed = false)
                            )
                            newItem = ""
                            errorMessage = ""
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFAFDBF5))
                ) {
                    Text("Ок")
                }
            }

            if (errorMessage.isNotEmpty()) {
                Spacer(modifier = Modifier.height(8.dp))
                Text(text = errorMessage, color = Color.Red, fontSize = 14.sp)
            }

            Spacer(modifier = Modifier.height(16.dp))
        }
    }




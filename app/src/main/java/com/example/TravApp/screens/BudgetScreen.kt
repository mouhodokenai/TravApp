package com.example.TravApp.screens

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.TravApp.data.Budget
import com.example.TravApp.data.TravViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BudgetEntryScreen(
    viewModel: TravViewModel,
    tripId: Long,
    onBack: () -> Unit,
) {
    var shouldNavigateBack by remember { mutableStateOf(false) }
    if (shouldNavigateBack) {
        LaunchedEffect(Unit) { onBack() }
    }

    LaunchedEffect(tripId) {
        viewModel.loadBudgets(tripId)
    }

    val budgetItems by viewModel.budgetItems.observeAsState(emptyList())

    var isAdding by remember { mutableStateOf(false) }
    var category by remember { mutableStateOf("") }
    var amount by remember { mutableStateOf("") }
    var currency by remember { mutableStateOf("₽") }
    var errorMessage by remember { mutableStateOf("") }
    var expanded by remember { mutableStateOf(false) }

    val currencyOptions = listOf("₽" to "Россия", "€" to "Евро", "$" to "США", "¥" to "Япония", "₸" to "Казахстан")

    val backgroundColor = Color(0xFFFFF1D7)
    val textColor = Color(0xFF000000)
    val buttonColor = Color(0xFFAFDBF5)
    val AccentColor1 = Color(0xFFDDA0DD)
    val AccentColor2 = Color(0xFF2AFC98)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundColor)
            .padding(16.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            IconButton(onClick = { onBack() }) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Назад",
                    tint = textColor
                )
            }
            Text(
                "Бюджет",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = textColor
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
        ) {
            items(budgetItems) { item ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp),
                    elevation = CardDefaults.cardElevation(4.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White)
                ) {
                    Row(
                        modifier = Modifier
                            .padding(12.dp)
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(item.category, color = textColor)
                            Text("${item.amount} ${item.currency}", color = textColor)
                        }
                        IconButton(onClick = {
                            viewModel.deleteBudget(item)
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
        }

        Spacer(modifier = Modifier.height(8.dp))

        Button(
            onClick = { isAdding = !isAdding },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(containerColor = buttonColor)
        ) {
            Text(if (isAdding) "Скрыть" else "Добавить", color = textColor)
        }

        if (isAdding) {
            Spacer(modifier = Modifier.height(8.dp))

            Column {
                OutlinedTextField(
                    value = category,
                    onValueChange = { category = it },
                    label = { Text("Категория", color = textColor) },
                    modifier = Modifier.fillMaxWidth(),
                    textStyle = TextStyle(color = textColor)
                )

                OutlinedTextField(
                    value = amount,
                    onValueChange = { amount = it },
                    label = { Text("Сумма", color = textColor) },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.fillMaxWidth(),
                    textStyle = TextStyle(color = textColor)
                )

                ExposedDropdownMenuBox(
                    expanded = expanded,
                    onExpandedChange = { expanded = !expanded },
                ) {
                    OutlinedTextField(
                        value = currency,
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Валюта", color = textColor) },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .menuAnchor(),
                        textStyle = TextStyle(color = textColor)
                    )
                    ExposedDropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false },
                    ) {
                        currencyOptions.forEach { (symbol, country) ->
                            DropdownMenuItem(
                                text = { Text("$symbol — $country") },
                                onClick = {
                                    currency = symbol
                                    expanded = false
                                }
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                Button(
                    onClick = {
                        if (category.isBlank()) {
                            errorMessage = "Введите категорию"
                        } else if (amount.isBlank()) {
                            errorMessage = "Введите сумму"
                        } else {
                            val parsedAmount = amount.toDoubleOrNull()
                            if (parsedAmount == null) {
                                errorMessage = "Сумма должна быть числом"
                            } else {
                                val newBudget = Budget(
                                    category = category,
                                    amount = parsedAmount,
                                    currency = currency,
                                    tripId = tripId
                                )
                                viewModel.addBudgetItem(newBudget)
                                category = ""
                                amount = ""
                                currency = "₽"
                                isAdding = false
                                errorMessage = ""
                            }
                        }
                    },
                    modifier = Modifier.align(Alignment.End),
                    colors = ButtonDefaults.buttonColors(containerColor = buttonColor)
                ) {
                    Text("Ок", color = textColor)
                }

                if (errorMessage.isNotBlank()) {
                    Text(errorMessage, color = Color.Red, fontSize = 14.sp)
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        val currencyTotals = budgetItems.groupBy { it.currency }
            .mapValues { (_, items) -> items.sumOf { it.amount } }

        Column(modifier = Modifier.fillMaxWidth()) {
            Text(
                text = "Общие суммы:",
                fontSize = 18.sp,
                fontWeight = FontWeight.SemiBold,
                color = textColor,
                modifier = Modifier.align(Alignment.End)
            )

            currencyTotals.forEach { (curr, sum) ->
                Text(
                    text = "$sum $curr",
                    fontSize = 16.sp,
                    color = textColor,
                    modifier = Modifier.align(Alignment.End)
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

        }
    }
}

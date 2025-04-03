package com.example.TravApp.screens

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.util.Log
import android.widget.TimePicker
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.testapp.R
import java.util.Calendar
import java.util.Date

@Composable
fun HotelEntryScreen(
    onSave: (String) -> Unit,
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

    var check_in  by remember { mutableStateOf("") }
    var check_out by remember { mutableStateOf("") }

    var adress by remember { mutableStateOf("") }
    var name by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .background(Color.White),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Spacer(modifier = Modifier.height(10.dp))

        Text(text = "Добавление жилья", fontSize = 24.sp, fontWeight = FontWeight.Bold)

        Spacer(modifier = Modifier.height(15.dp))

        OutlinedTextField(
            value = name,
            onValueChange = { name = it },
            placeholder = { Text("Название",
                color = Color.Black,
                fontFamily = FontFamily.Default,
                fontSize = 15.sp) },
            modifier = Modifier.fillMaxWidth().height(50.dp)
        )

        Spacer(modifier = Modifier.height(15.dp))

        OutlinedTextField(
            value = adress,
            onValueChange = { adress = it },
            placeholder = { Text("Адресс",
                color = Color.Black,
                fontFamily = FontFamily.Default,
                fontSize = 15.sp) },

            modifier = Modifier.fillMaxWidth().height(50.dp)
        )

        Spacer(modifier = Modifier.height(15.dp))

        OutlinedButton(
            onClick = {
                DatePickerDialog(
                    context,
                    { _, year, month, dayOfMonth ->
                        val selectedDate = "$dayOfMonth.${month + 1}.$year"
                        check_in = selectedDate

                    },
                    calendar.get(Calendar.YEAR),
                    calendar.get(Calendar.MONTH),
                    calendar.get(Calendar.DAY_OF_MONTH)
                ).show()
            },
            modifier = Modifier.fillMaxWidth().height(55.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color.White),
            shape = RectangleShape
        ) {
            Text(
                text = "Заезд: $check_in",
                modifier = Modifier.fillMaxWidth(),
                color = Color.Black,
                textAlign = TextAlign.Start,
                fontWeight = FontWeight.Normal,
                fontSize = 15.sp
            )
        }

        Spacer(modifier = Modifier.height(15.dp))

        OutlinedButton(
            onClick = {
                DatePickerDialog(
                    context,
                    { _, year, month, dayOfMonth ->
                        val selectedDate = "$dayOfMonth.${month + 1}.$year"
                        check_out = selectedDate

                    },
                    calendar.get(Calendar.YEAR),
                    calendar.get(Calendar.MONTH),
                    calendar.get(Calendar.DAY_OF_MONTH)
                ).show()
            },
            modifier = Modifier.fillMaxWidth().height(55.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color.White),
            shape = RectangleShape
        ) {
            Text(
                text = "Отъезд: $check_out",
                modifier = Modifier.fillMaxWidth(),
                color = Color.Black,
                textAlign = TextAlign.Start,
                fontWeight = FontWeight.Normal,
                fontSize = 15.sp
            )
        }

        Spacer(modifier = Modifier.height(15.dp))

        Spacer(modifier = Modifier.height(20.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween

        ) {
            Button(onClick =
            onBack,
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF90CAF9)
                )
            ) {
                Text("Назад")
            }

            Button(onClick = {
                Log.d("HotelEntry", "About to pop back stack")
                onSave(name)
                shouldNavigateBack = true
            }, colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF90CAF9)
            )) {
                Text("Сохранить")
            }
        }
    }
}



@Preview(showBackground = true)
@Composable
fun HotelEntryScreenPreview() {
    HotelEntryScreen(
        onSave = { _ -> },
        onBack = {},
    )
}

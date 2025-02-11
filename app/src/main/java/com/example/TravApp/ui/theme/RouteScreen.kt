package com.example.TravApp.ui.theme

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.TravApp.data.Route
import com.example.TravApp.viewmodel.RouteViewModel
import com.example.testapp.NewTripScreen

class RouteScreenActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            RouteScreen()
        }
    }
}
@Composable
fun RouteScreen() {
    // Используем viewModel() для получения экземпляра RouteViewModel
    val viewModel: RouteViewModel = viewModel()

    // Получаем данные из LiveData
    val routes by viewModel.allRoutes.observeAsState(emptyList())

    Column(modifier = Modifier.padding(16.dp)) {
        routes.forEach { route ->
            Text(text = route.name, style = MaterialTheme.typography.bodyLarge)
        }

        Button(onClick = {
            // Пример добавления маршрута
            viewModel.insert(Route(name = "Новый маршрут", points = listOf("Точка 1", "Точка 2")))
        }) {
            Text("Добавить маршрут")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewRouteScreen() {
    RouteScreen()
}

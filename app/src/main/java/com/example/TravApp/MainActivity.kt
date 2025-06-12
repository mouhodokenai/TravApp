package com.example.testapp
import android.app.Application
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.TravApp.data.TravViewModel
import com.example.TravApp.data.TravViewModelFactory
import com.example.TravApp.screens.BaggageEntryScreen
import com.example.TravApp.screens.ArchiveDashboard
import com.example.TravApp.screens.BudgetEntryScreen
import com.example.TravApp.screens.HotelEntryScreen
import com.example.TravApp.screens.NewTrip
import com.example.TravApp.screens.NewTripDetails
import com.example.TravApp.screens.NoteScreen
import com.example.TravApp.screens.PlanDashboard
import com.example.TravApp.screens.RouteScreen
import com.example.TravApp.screens.TicketEntryScreen

import com.example.TravApp.screens.TravelDashboard
import com.example.testapp.ui.theme.TravAppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TravAppTheme {
                val navController = rememberNavController()
                val context = LocalContext.current
                val vmFactory =
                    TravViewModelFactory(context.applicationContext as Application)
                val viewModel: TravViewModel = viewModel(factory = vmFactory)
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    NavHost(navController = navController, startDestination = "home") {
                        composable("home") {
                            TravelDashboard(
                                onNavigateToNewTrip = { navController.navigate("new") },
                                onNavigateToPlans = { navController.navigate("plan") },
                                onNavigateToArchive = { navController.navigate("archive") }
                            )
                        }
                        composable("plan") {
                            PlanDashboard(
                                viewModel = viewModel,
                                onNavigateToNewTrip = { navController.navigate("new") },
                                onNavigateToEditTrip = {Id, tripName -> navController.navigate("new_details/$Id/$tripName") },
                                onNavigateBack = { navController.popBackStack() }
                            )
                        }
                        composable("archive") {
                            ArchiveDashboard(
                                viewModel = viewModel,
                                onNavigateToNewTrip = { navController.navigate("new") },
                                onNavigateToEditTrip = {Id, tripName -> navController.navigate("new_details/$Id/$tripName") },
                                onNavigateBack = { navController.popBackStack() }
                            )
                        }
                        composable("new") {
                            NewTrip(
                                viewModel = viewModel,
                                onNavigateToDetails = {Id, tripName -> navController.navigate("new_details/$Id/$tripName") },
                                onNavigateBack = { navController.popBackStack() }
                            )
                        }
                        composable("new_details/{Id}/{tripName}",) {
                                backStackEntry ->
                            val tripId = backStackEntry.arguments?.getLong("Id") ?: 0
                            val title = backStackEntry.arguments?.getString("tripName") ?: "No"
                            NewTripDetails(
                                title = title,
                                tripId = tripId,
                                onNavigateBack = { navController.popBackStack() },
                                onNavigateHome = {navController.navigate("home")}
                            )
                        }
                        composable("route/{tripId}") { backStackEntry ->
                            val tripId =
                                backStackEntry.arguments?.getString("tripId")?.toLongOrNull()
                                    ?: return@composable

                            RouteScreen(
                                viewModel = viewModel,
                                tripId = tripId,
                                onBack = { navController.popBackStack() }
                            )
                        }
                        composable("tickets/{tripId}") {
                                backStackEntry ->
                            val tripId =
                                backStackEntry.arguments?.getString("tripId")?.toIntOrNull()
                                    ?: return@composable

                            TicketEntryScreen(
                                viewModel = viewModel,
                                tripId = tripId.toLong(),
                                onBack = { navController.popBackStack() }
                            )
                        }
                        composable("hotels/{tripId}") {
                                backStackEntry ->
                            val tripId =
                                backStackEntry.arguments?.getString("tripId")?.toLongOrNull()
                                    ?: return@composable

                            HotelEntryScreen(
                                viewModel = viewModel,
                                tripId = tripId,
                                onBack = { navController.popBackStack() }
                            )
                        }
                        composable("budget") {
                                backStackEntry ->
                            val tripId =
                                backStackEntry.arguments?.getString("tripId")?.toLongOrNull()
                                    ?: return@composable
                            BudgetEntryScreen(
                                viewModel = viewModel,
                                tripId = tripId,
                                onBack = { navController.popBackStack() }
                            )
                        }
                        composable("baggage/{tripId}") {
                                backStackEntry ->
                            val tripId =
                                backStackEntry.arguments?.getString("tripId")?.toLongOrNull()
                                    ?: return@composable

                            BaggageEntryScreen(
                                viewModel = viewModel,
                                tripId = tripId,
                                onBack = { navController.popBackStack() }
                            )
                        }
                        composable("note/{tripId}") {
                            backStackEntry ->
                            val tripId =
                                backStackEntry.arguments?.getString("tripId")?.toLongOrNull()
                                    ?: return@composable

                            NoteScreen(
                                viewModel = viewModel,
                                tripId = tripId,
                                onBack = { navController.popBackStack() }
                            )
                        }
                    }
                }
            }
        }
    }
}


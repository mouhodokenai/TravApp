package com.example.testapp
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.ui.Modifier
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.TravApp.screens.ArchiveDashboard
import com.example.TravApp.screens.HotelEntryScreen
import com.example.TravApp.screens.NewTrip
import com.example.TravApp.screens.NewTripDetails
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
                                onNavigateToNewTrip = { navController.navigate("new") },
                                onNavigateBack = { navController.popBackStack() }
                            )
                        }
                        composable("archive") {
                            ArchiveDashboard(
                                onNavigateToNewTrip = { navController.navigate("new") },
                                onNavigateBack = { navController.popBackStack() }
                            )
                        }
                        composable("new") {
                            NewTrip(
                                onNavigateToDetails = { tripName, startDate, endDate ->
                                    navController.navigate("new_details/$tripName/$startDate/$endDate")
                                },
                                onNavigateBack = { navController.popBackStack() }
                            )
                        }
                        composable("route") {
                            RouteScreen(
                                onPlaceSelected = { placeName, latLng ->
                                    navController.previousBackStackEntry
                                        ?.savedStateHandle
                                        ?.set("selectedPlace", Pair(placeName, latLng)) // Передаём пару (название, координаты)
                                    navController.popBackStack()
                                },
                                onBack = { navController.popBackStack() }
                            )
                        }
                        composable("tickets") {
                            Log.d("Navigation", "TicketEntryScreen открывается")
                            TicketEntryScreen(
                                onSelected = { departureCity, arrivalCity ->
                                    Log.d("Navigation", "Before navigate: ${navController.currentBackStackEntry?.destination?.route}")
                                    navController.previousBackStackEntry
                                        ?.savedStateHandle
                                        ?.set("selectedTicket", Pair(departureCity, arrivalCity))
                                    navController.popBackStack()
                                    Log.d("Navigation", "After navigate: ${navController.currentBackStackEntry?.destination?.route}")
                                },
                                onBack = { navController.popBackStack() }
                            )
                        }
                        composable("hotels") {
                            HotelEntryScreen(
                                onSave = { name ->

                                    navController.previousBackStackEntry
                                        ?.savedStateHandle
                                        ?.set("selectedTicket", name)

                                    navController.popBackStack()
                                },
                                onBack = { navController.popBackStack() }
                            )
                        }

                        composable(
                            "new_details/{tripName}/{startDate}/{endDate}",
                            arguments = listOf(
                                navArgument("tripName") { type = NavType.StringType },
                                navArgument("startDate") { type = NavType.StringType },
                                navArgument("endDate") { type = NavType.StringType }
                            )
                        ) { backStackEntry ->
                            val tripName = backStackEntry.arguments?.getString("tripName") ?: ""
                            val startDate = backStackEntry.arguments?.getString("startDate") ?: ""
                            val endDate = backStackEntry.arguments?.getString("endDate") ?: ""

                            NewTripDetails(
                                tripName = tripName,
                                startDate = startDate,
                                endDate = endDate,
                                onNavigateBack = { navController.popBackStack() },
                                onNavigateToRoute = { navController.navigate("route") },
                                onNavigateToTickets = { navController.navigate("tickets")},
                                onNavigateToHotels = { navController.navigate("hotels")},
                                onNavigateToBudget = { navController.navigate("budget")},
                                onNavigateToBaggage = { navController.navigate("baggage")},
                                onNavigateToNotes = { navController.navigate("notes")}
                            )
                        }

                    }
                }
            }
        }
    }
}

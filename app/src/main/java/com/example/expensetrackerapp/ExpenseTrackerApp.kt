package com.example.expensetrackerapp

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.expensetrackerapp.components.appbar.BottomNavBar
import com.example.expensetrackerapp.components.appbar.BottomNavItem
import com.example.expensetrackerapp.data.getToken
import com.example.expensetrackerapp.screens.MainScreen


@Composable
fun ExpenseTrackerApp(
    appViewModel: ExpenseTrackerViewModel = viewModel(),
    navController: NavHostController = rememberNavController()
) {
    val appUiState by appViewModel.uiState.collectAsState()
    val context = LocalContext.current
    val currentRoute = navController.currentBackStackEntryAsState().value?.destination?.route
    println("CurrentRoute: $currentRoute")
    Scaffold(
        bottomBar = {
            if (currentRoute !in listOf(BottomNavItem.Login.route, BottomNavItem.Loading.route, BottomNavItem.Validating.route, null)) {
                BottomNavBar(
                    navigateTo = { route ->
                        navController.navigate(route) {
                            popUpTo(navController.graph.startDestinationId) {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    },
                    currentRoute = currentRoute
                )
            }
        }
    ) { innerPadding ->
        NavHost(navController = navController, startDestination = BottomNavItem.Validating.route, Modifier.padding(innerPadding)) {
            composable(BottomNavItem.Home.route) { Greeting(name = "HOME") }
            composable(BottomNavItem.Statistics.route) { Greeting(name = "STATS")}
            composable(BottomNavItem.Profile.route) { Greeting(name = "PROFILE") }
            composable(BottomNavItem.Validating.route) {
                MainScreen(
                    context = context,
                    loginOnClick = { username, password ->
                        appViewModel.setUsernamePassword(username, password)
                        appViewModel.performLogin(
                            context = context,
                            onSuccess = { token ->
                                println("Token: $token")
                                val sharedToken = getToken(context)
                                println("Shared Pref: $sharedToken")
                                navController.navigate(BottomNavItem.Home.route) {
                                    popUpTo(navController.graph.startDestinationId) {
                                        saveState = true
                                    }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            },
                            onError = { error ->
                                println("Error: $error")
                            }
                        )
                    },
                    validateToken = {
                        val token = getToken(context)
                        if (token != null) {
                            appViewModel.setToken(token)
                        }
                        appViewModel.validateToken(context)
                        appUiState.isTokenValid
                    },
                    goToHome = {
                        navController.navigate(BottomNavItem.Home.route) {
                            popUpTo(navController.graph.startDestinationId) {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                )
            }
        }
    }

}

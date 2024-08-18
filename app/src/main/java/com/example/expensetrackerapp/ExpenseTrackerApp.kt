package com.example.expensetrackerapp

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.expensetrackerapp.components.CreateExpensePopup
import com.example.expensetrackerapp.components.appbar.BottomNavBar
import com.example.expensetrackerapp.components.appbar.BottomNavItem
import com.example.expensetrackerapp.data.getToken
import com.example.expensetrackerapp.data.removeToken
import com.example.expensetrackerapp.data.removeUsername
import com.example.expensetrackerapp.data.saveUsername
import com.example.expensetrackerapp.screens.HomeScreen
import com.example.expensetrackerapp.screens.LoadingScreen
import com.example.expensetrackerapp.screens.MainScreen
import com.example.expensetrackerapp.screens.ProfileScreen
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


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
            composable(BottomNavItem.Home.route) {
                var isLoading by remember { mutableStateOf(true) }
                LaunchedEffect(Unit) {
                    appViewModel.getExpensesFromApi(context)
                    appViewModel.getCategoriesFromApi(context)
                    isLoading = false
                }
                if (isLoading) {
                    LoadingScreen()
                } else {
                    HomeScreen(
                        expenses = appViewModel.getExpenses(),
                        categories = appViewModel.getCategories()
                    )
                }
            }
            composable(BottomNavItem.AddExpense.route) {
                CreateExpensePopup(
                    onSave = { amount, category, description, date ->
                        // Launch a coroutine in the appropriate scope to call the suspend function
                        CoroutineScope(Dispatchers.IO).launch {
                            try {
                                appViewModel.createExpense(context, amount, category, description, date)

                                // Once the expense is created, navigate to the Home screen
                                withContext(Dispatchers.Main) {
                                    navController.navigate(BottomNavItem.Home.route) {
                                        popUpTo(navController.graph.startDestinationId) {
                                            saveState = true
                                        }
                                        launchSingleTop = true
                                        restoreState = true
                                    }
                                }
                            } catch (e: Exception) {
                                println("Error creating expense: ${e.message}")
                            }
                        }
                    },
                    onDismiss = {
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
            composable(BottomNavItem.Statistics.route) { Greeting(name = "STATS")}
            composable(BottomNavItem.Profile.route) {
                ProfileScreen(
                    logoutOnClick = {
                        removeToken(context = context)
                        removeUsername(context = context)
                        navController.navigate(BottomNavItem.Validating.route) {
                            popUpTo(navController.graph.startDestinationId) {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                )
            }
            composable(BottomNavItem.Validating.route) {
                MainScreen(
                    context = context,
                    loginOnClick = { username, password ->
                        appViewModel.setUsernamePassword(username, password)
                        CoroutineScope(Dispatchers.IO).launch {
                            appViewModel.performLogin(
                                context = context,
                                onSuccess = {
                                    saveUsername(context, username)
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
                        }
                    },
                    validateToken = suspend {
                        val token = getToken(context)
                        if (token != null) {
                            appViewModel.setToken(token)
                        }
                        val isValid = appViewModel.validateToken(context)
                        appViewModel.isTokenValid()
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

package com.example.expensetrackerapp

import android.os.Build
import androidx.annotation.RequiresApi
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
import com.example.expensetrackerapp.components.appbar.BottomNavBar
import com.example.expensetrackerapp.components.appbar.AppScreen
import com.example.expensetrackerapp.components.appbar.MyTopAppBar
import com.example.expensetrackerapp.components.defaultEnterTransition
import com.example.expensetrackerapp.components.defaultExitTransition
import com.example.expensetrackerapp.data.getToken
import com.example.expensetrackerapp.data.removeToken
import com.example.expensetrackerapp.data.removeUsername
import com.example.expensetrackerapp.data.saveUsername
import com.example.expensetrackerapp.screens.CreateExpenseScreen
import com.example.expensetrackerapp.screens.ExpenseByDateScreen
import com.example.expensetrackerapp.screens.HomeScreen
import com.example.expensetrackerapp.screens.LoadingScreen
import com.example.expensetrackerapp.screens.LoginScreen
import com.example.expensetrackerapp.screens.MainScreen
import com.example.expensetrackerapp.screens.ProfileScreen
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@RequiresApi(Build.VERSION_CODES.O)
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
            if (currentRoute !in listOf(AppScreen.Login.route, AppScreen.Loading.route, AppScreen.Validating.route, null)) {
                BottomNavBar(
                    navigateTo = { route ->
                        navController.navigate(route) {
                            popUpTo(AppScreen.Home.route) {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    },
                    currentRoute = currentRoute
                )
            }
        },
        topBar = {
            if (currentRoute !in listOf(AppScreen.Login.route, AppScreen.Loading.route, AppScreen.Validating.route, null)) {
                if (currentRoute != null) {
                    MyTopAppBar(
                        currentScreen = currentRoute.replaceFirstChar(Char::titlecase))
                }
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController, startDestination = AppScreen.Validating.route, Modifier.padding(innerPadding),
        ) {
            composable(
                AppScreen.Home.route,
                enterTransition = defaultEnterTransition(),
                exitTransition = defaultExitTransition(),
                popEnterTransition = defaultEnterTransition(),
                popExitTransition = defaultExitTransition()
            ) {
                var isLoading by remember { mutableStateOf(true) }
                LaunchedEffect(Unit) {
                    appViewModel.getExpensesFromApi(context)
                    appViewModel.getCategoriesFromApi(context)
                    appViewModel.getSpendingLimitFromApi(context)
                    appViewModel.calculateExpenseByCategory()
                    appViewModel.setListOfExpenseByCategory()
                    appViewModel.calculateCurrentMonthExpense()
                    isLoading = false
                }
                if (isLoading) {
                    LoadingScreen()
                } else {
                    HomeScreen(
                        expenseByCategory = appViewModel.getListOfExpenseByCategory(),
                        monthlyLimit = appViewModel.getSpendingLimit(),
                        currentMonthExpense = appViewModel.getCurrentMonthExpense(),
                        onSetLimit = { limit ->
                            CoroutineScope(Dispatchers.IO).launch {
                                appViewModel.setSpendingLimitFromApi(limit, context)
                            }
                        },
                        onDateSelected = {date ->
                            appViewModel.setExpensesByDate(date)
                            navController.navigate(AppScreen.ExpenseByDate.route) {
                                popUpTo(AppScreen.Home.route) {
                                    saveState = true
                                    inclusive = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        }
                    )
                }
            }
            composable(
                AppScreen.AddExpense.route,
                enterTransition = defaultEnterTransition(),
                exitTransition = defaultExitTransition(),
                popEnterTransition = defaultEnterTransition(),
                popExitTransition = defaultExitTransition()
            ) {
                CreateExpenseScreen(
                    onSave = { amount, category, description, date ->
                        try {
                            appViewModel.createExpense(context, amount, category, description, date)
                        } catch (e: Exception) {
                            println("Error creating expense: ${e.message}")
                            false
                        }
                    },
                    onDismiss = {
                        navController.navigate(AppScreen.Home.route) {
                            popUpTo(AppScreen.Home.route) {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    },
                    categoryNameMap = appViewModel.getCategoriesNameMap()
                )
            }
            composable(
                AppScreen.Statistics.route,
                enterTransition = defaultEnterTransition(),
                exitTransition = defaultExitTransition(),
                popEnterTransition = defaultEnterTransition(),
                popExitTransition = defaultExitTransition()
            ) { Greeting(name = "STATS")}

            composable(AppScreen.Profile.route) {
                ProfileScreen(
                    logoutOnClick = {
                        removeToken(context = context)
                        removeUsername(context = context)
                        navController.navigate(AppScreen.Validating.route) {
                            popUpTo(AppScreen.Home.route) {
                                saveState = true
                                inclusive = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                )
            }
            composable(
                AppScreen.Validating.route,
                enterTransition = defaultEnterTransition(),
                exitTransition = defaultExitTransition(),
                popEnterTransition = defaultEnterTransition(),
                popExitTransition = defaultExitTransition()
            ) {
                MainScreen(
                    context = context,
                    validateToken = suspend {
                        val token = getToken(context)
                        if (token != null) {
                            appViewModel.setToken(token)
                        }
                        val isValid = appViewModel.validateToken(context)
                        appViewModel.isTokenValid()
                    },
                    goToHome = {
                        navController.navigate(AppScreen.Home.route) {
                            popUpTo(AppScreen.Validating.route) {
                                saveState = true
                                inclusive = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    },
                    goToLogin = {
                        navController.navigate(AppScreen.Login.route) {
                            popUpTo(AppScreen.Validating.route) {
                                inclusive = true
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                )
            }

            composable(
                AppScreen.ExpenseByDate.route,
                enterTransition = defaultEnterTransition(),
                exitTransition = defaultExitTransition(),
                popEnterTransition = defaultEnterTransition(),
                popExitTransition = defaultExitTransition()
            ) {
                ExpenseByDateScreen(
                    expenses = appViewModel.getExpensesByDate(),
                    categories = appViewModel.getCategories()
                )
            }
            composable(
                AppScreen.Login.route,
                enterTransition = defaultEnterTransition(),
                exitTransition = defaultExitTransition(),
                popEnterTransition = defaultEnterTransition(),
                popExitTransition = defaultExitTransition()
            ) {
                val (isLoading, setLoading) = remember { mutableStateOf(false) }
                val (errorMessage, setErrorMessage) = remember { mutableStateOf<String?>(null) }

                LoginScreen(
                    isLoading = isLoading,
                    errorMessage = errorMessage,
                    loginOnClick = { username, password ->
                        setLoading(true)
                        setErrorMessage(null)

                        appViewModel.setUsernamePassword(username, password)

                        CoroutineScope(Dispatchers.IO).launch {
                            appViewModel.performLogin(
                                context = context,
                                onSuccess = {
                                    saveUsername(context, username)
                                    navController.navigate(AppScreen.Home.route) {
                                        popUpTo(AppScreen.Login.route) {
                                            inclusive = true
                                        }
                                        launchSingleTop = true
                                    }
                                    setLoading(false) // Hide loading indicator on success
                                },
                                onError = { error ->
                                    println("Error: $error")
                                    if(error=="HTTP 401 ") {
                                        setErrorMessage("Username or password is wrong")
                                    }
                                    else {
                                        setErrorMessage("Server Issue. Please try again later")
                                    }
                                    setLoading(false)
                                }
                            )
                        }
                    }
                )
            }
        }
    }
}

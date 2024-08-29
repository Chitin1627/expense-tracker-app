package com.example.expensetrackerapp

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
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
import com.example.expensetrackerapp.data.viewmodels.AddExpenseViewModel
import com.example.expensetrackerapp.data.viewmodels.AuthenticationViewModel
import com.example.expensetrackerapp.data.viewmodels.HomeScreenViewModel
import com.example.expensetrackerapp.screens.MainScreen
import com.example.expensetrackerapp.screens.helper.CreateExpenseScreenHelper
import com.example.expensetrackerapp.screens.helper.ExpenseByDateScreenHelper
import com.example.expensetrackerapp.screens.helper.HomeScreenHelper
import com.example.expensetrackerapp.screens.helper.LoginScreenHelper
import com.example.expensetrackerapp.screens.helper.MainScreenHelper
import com.example.expensetrackerapp.screens.helper.ProfileScreenHelper
import com.example.expensetrackerapp.screens.helper.RegisterScreenHelper

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ExpenseTrackerApp(
    navController: NavHostController = rememberNavController()
) {
    //val appUiState by appViewModel.uiState.collectAsState()
    val homeScreenViewModel: HomeScreenViewModel = viewModel()
    val context = LocalContext.current
    //removeToken(context)
    val currentRoute = navController.currentBackStackEntryAsState().value?.destination?.route
    Scaffold(
        bottomBar = {
            if (currentRoute !in listOf(AppScreen.Login.route, AppScreen.Register.route, AppScreen.Loading.route, AppScreen.Validating.route, null)) {
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
            if (currentRoute !in listOf(AppScreen.Login.route, AppScreen.Register.route, AppScreen.Loading.route, AppScreen.Validating.route, null)) {
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
                HomeScreenHelper(navController = navController, homeScreenViewModel = homeScreenViewModel, context = context)
            }


            composable(
                AppScreen.AddExpense.route,
                enterTransition = defaultEnterTransition(),
                exitTransition = defaultExitTransition(),
                popEnterTransition = defaultEnterTransition(),
                popExitTransition = defaultExitTransition()
            ) {
                CreateExpenseScreenHelper(
                    navController = navController,
                    homeScreenViewModel = homeScreenViewModel,
                    context = context,
                    categoryNameMap = homeScreenViewModel.getCategoriesNameMap()
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
                ProfileScreenHelper(navController = navController, context = context)
            }


            composable(
                AppScreen.Validating.route,
                enterTransition = defaultEnterTransition(),
                exitTransition = defaultExitTransition(),
                popEnterTransition = defaultEnterTransition(),
                popExitTransition = defaultExitTransition()
            ) {
                MainScreenHelper(navController = navController, context = context)
            }


            composable(
                AppScreen.ExpenseByDate.route,
                enterTransition = defaultEnterTransition(),
                exitTransition = defaultExitTransition(),
                popEnterTransition = defaultEnterTransition(),
                popExitTransition = defaultExitTransition()
            ) {
                ExpenseByDateScreenHelper(navController = navController, homeScreenViewModel = homeScreenViewModel, context = context)
            }


            composable(
                AppScreen.Login.route,
                enterTransition = defaultEnterTransition(),
                exitTransition = defaultExitTransition(),
                popEnterTransition = defaultEnterTransition(),
                popExitTransition = defaultExitTransition()
            ) {
                LoginScreenHelper(navController = navController, context = context)
            }

            composable(
                AppScreen.Register.route,
                enterTransition = defaultEnterTransition(),
                exitTransition = defaultExitTransition(),
                popEnterTransition = defaultEnterTransition(),
                popExitTransition = defaultExitTransition()
            ) {
                RegisterScreenHelper(navController = navController, context = context)
            }
        }
    }
}

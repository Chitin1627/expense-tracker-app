package com.example.expensetrackerapp.screens.helper

import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.expensetrackerapp.components.appbar.AppScreen
import com.example.expensetrackerapp.data.viewmodels.HomeScreenViewModel
import com.example.expensetrackerapp.screens.HomeScreen
import com.example.expensetrackerapp.screens.LoadingScreen
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun HomeScreenHelper(
    navController: NavHostController,
    homeScreenViewModel: HomeScreenViewModel,
    context: Context
) {
    var isLoading by remember { mutableStateOf(true) }
    LaunchedEffect(Unit) {
        if(!homeScreenViewModel.getIsDataLoaded()) {
            val response = homeScreenViewModel.getExpensesCurrentMonthFromApi(context)
            homeScreenViewModel.getCategoriesFromApi(context)
            homeScreenViewModel.calculateExpenseByCategory(response)
            homeScreenViewModel.calculateCurrentMonthExpense(response)
            homeScreenViewModel.getSpendingLimitFromApi(context)
            homeScreenViewModel.setListOfExpenseByCategory()
        }
        isLoading = false
    }


    if (isLoading) {
        LoadingScreen()
    } else {
        HomeScreen(
            expenseByCategory = homeScreenViewModel.getListOfExpenseByCategory(),
            monthlyLimit = homeScreenViewModel.getSpendingLimit(),
            currentMonthExpense = homeScreenViewModel.getCurrentMonthExpense(),
            netExpense = homeScreenViewModel.getNetExpense(),
            onSetLimit = { limit ->
                CoroutineScope(Dispatchers.IO).launch {
                    homeScreenViewModel.setSpendingLimitFromApi(limit, context)
                    homeScreenViewModel.setIsDataLoaded(false)
                }
            },
            onDateSelected = {date ->
                homeScreenViewModel.setSelectedDate(date)
                navController.navigate(AppScreen.ExpenseByDate.route) {
                    popUpTo(AppScreen.Home.route) {
                        saveState = true
                    }
                    launchSingleTop = true
                    restoreState = true
                }
            }
        )
    }
}
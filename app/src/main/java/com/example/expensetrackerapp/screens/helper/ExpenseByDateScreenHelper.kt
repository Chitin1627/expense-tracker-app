package com.example.expensetrackerapp.screens.helper

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.expensetrackerapp.data.viewmodels.HomeScreenViewModel
import com.example.expensetrackerapp.model.Expense
import com.example.expensetrackerapp.screens.ExpenseByDateScreen
import com.example.expensetrackerapp.screens.LoadingScreen
import kotlin.math.exp

@Composable
fun ExpenseByDateScreenHelper(
    navController: NavHostController,
    homeScreenViewModel: HomeScreenViewModel,
    context: Context
) {
    var isLoading by remember { mutableStateOf(true) }
    var expenseByDate by remember { mutableStateOf(emptyList<Expense>()) }
    LaunchedEffect(Unit) {
        val date = homeScreenViewModel.getSelectedDate()
        println(date)
        expenseByDate = homeScreenViewModel.getExpensesByDateFromApi(context, date)
        isLoading = false
    }

    if(isLoading) {
        LoadingScreen()
    }
    else {
        ExpenseByDateScreen(
            expenses = expenseByDate,
            categories = homeScreenViewModel.getCategories()
        )
    }

}
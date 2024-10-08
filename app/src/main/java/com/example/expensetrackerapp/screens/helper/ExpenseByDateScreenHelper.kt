package com.example.expensetrackerapp.screens.helper

import android.annotation.SuppressLint
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
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlin.math.exp

@SuppressLint("MutableCollectionMutableState")
@Composable
fun ExpenseByDateScreenHelper(
    navController: NavHostController,
    homeScreenViewModel: HomeScreenViewModel,
    context: Context
) {
    var isLoading by remember { mutableStateOf(true) }
    var expenseByDate by remember { mutableStateOf(ArrayList<Expense>()) }
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
            date = homeScreenViewModel.getSelectedDate(),
            expenses = expenseByDate,
            homeScreenViewModel = homeScreenViewModel,
            categories = homeScreenViewModel.getCategories(),
            onExpenseDelete = {expenseId ->
                CoroutineScope(Dispatchers.IO).launch {
                    homeScreenViewModel.deleteExpense(context, expenseId)
                    homeScreenViewModel.setIsDataLoaded(false)
                }
            }
        )
    }
}
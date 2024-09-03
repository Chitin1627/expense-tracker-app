package com.example.expensetrackerapp.screens.helper

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.expensetrackerapp.components.appbar.AppScreen
import com.example.expensetrackerapp.data.viewmodels.AddExpenseViewModel
import com.example.expensetrackerapp.data.viewmodels.HomeScreenViewModel
import com.example.expensetrackerapp.screens.CreateExpenseScreen

@Composable
fun CreateExpenseScreenHelper(
    navController: NavHostController,
    homeScreenViewModel: HomeScreenViewModel,
    context: Context,
    categoryNameMap: HashMap<String, String>
) {
    val addExpenseViewModel: AddExpenseViewModel = viewModel()
    CreateExpenseScreen(
        onSave = { expenseId, amount, category, type, description, date, created_at ->
            addExpenseViewModel.setExpenseId(expenseId)
            addExpenseViewModel.setAmount(amount)
            addExpenseViewModel.setCategory(category)
            addExpenseViewModel.setDescription(description)
            addExpenseViewModel.setDate(date)
            addExpenseViewModel.setType(type)
            try {
                val success = addExpenseViewModel.createExpense(context)
                if(success) homeScreenViewModel.setIsDataLoaded(false)
                success
            } catch (e: Exception) {
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
        categoryNameMap = categoryNameMap
    )
}
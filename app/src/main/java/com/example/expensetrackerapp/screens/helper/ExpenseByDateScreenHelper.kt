package com.example.expensetrackerapp.screens.helper

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.expensetrackerapp.data.viewmodels.HomeScreenViewModel
import com.example.expensetrackerapp.screens.ExpenseByDateScreen

@Composable
fun ExpenseByDateScreenHelper(
    navController: NavHostController,
    homeScreenViewModel: HomeScreenViewModel,
    context: Context
) {
    ExpenseByDateScreen(
        expenses = homeScreenViewModel.getExpensesByDate(),
        categories = homeScreenViewModel.getCategories()
    )
}
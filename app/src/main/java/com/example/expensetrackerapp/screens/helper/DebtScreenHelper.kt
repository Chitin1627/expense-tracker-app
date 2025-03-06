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
import com.example.expensetrackerapp.components.LoadingAnimation
import com.example.expensetrackerapp.data.viewmodels.DebtScreenViewModel
import com.example.expensetrackerapp.screens.DebtScreen

@Composable
fun DebtScreenHelper(
    navController: NavHostController,
    context: Context
) {
    val debtScreenViewModel: DebtScreenViewModel = viewModel()
    var isLoading by remember { mutableStateOf(true) }

    LaunchedEffect(Unit) {
        debtScreenViewModel.fetchDebts(context)
        isLoading = false
    }

    if (isLoading) {
        LoadingAnimation()
    } else {
        DebtScreen(
            debts = debtScreenViewModel.getDebts(),
            addDebt = { debt ->
                debtScreenViewModel.addDebt(debt, context)
            }
        )
    }
}

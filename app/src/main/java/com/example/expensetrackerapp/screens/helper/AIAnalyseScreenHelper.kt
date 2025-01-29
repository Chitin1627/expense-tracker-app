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
import com.example.expensetrackerapp.components.appbar.AppScreen
import com.example.expensetrackerapp.data.removeToken
import com.example.expensetrackerapp.data.removeUsername
import com.example.expensetrackerapp.data.viewmodels.AIAnalysisViewModel
import com.example.expensetrackerapp.data.viewmodels.AuthenticationViewModel
import com.example.expensetrackerapp.data.viewmodels.HomeScreenViewModel
import com.example.expensetrackerapp.model.PasswordChangeRequest
import com.example.expensetrackerapp.screens.AIAnalysisScreen
import com.example.expensetrackerapp.screens.LoadingScreen
import com.example.expensetrackerapp.screens.ProfileScreen

@Composable
fun AIAnalysisScreenHelper(
    navController: NavHostController,
    homeScreenViewModel: HomeScreenViewModel,
    context: Context
) {
    var isLoading by remember { mutableStateOf(true) }
    var response by remember { mutableStateOf("") }
    val aiAnalysisViewModel: AIAnalysisViewModel = viewModel()
    LaunchedEffect(Unit) {
        val resp = aiAnalysisViewModel.askAi(context)
        if (resp) {
            response = aiAnalysisViewModel.getResponse()
        }
        isLoading = false
    }

    if(isLoading) {
        LoadingScreen()
    }
    else {
        AIAnalysisScreen(
            response = response,
        )
    }
}

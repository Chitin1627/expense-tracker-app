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
import com.example.expensetrackerapp.components.extractKeyPoints
import com.example.expensetrackerapp.data.viewmodels.AIAnalysisViewModel
import com.example.expensetrackerapp.data.viewmodels.HomeScreenViewModel
import com.example.expensetrackerapp.screens.AIAnalysisScreen


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
        var resp = false
        while(!resp)  {
            resp = aiAnalysisViewModel.askAi(context)
        }

        response = if (resp) {
            aiAnalysisViewModel.getResponse()
        } else {
            "Something went wrong. Blame Gemini or Render, not me :)"
        }
        isLoading = false
    }

    if(isLoading) {
        LoadingAnimation()
    }
    else {
        val keyPoints = extractKeyPoints(response)
        AIAnalysisScreen(
            keyPoints = keyPoints,
        )
    }
}

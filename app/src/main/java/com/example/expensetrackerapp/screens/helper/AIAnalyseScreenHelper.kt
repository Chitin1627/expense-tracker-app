package com.example.expensetrackerapp.screens.helper

import android.content.Context
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Card
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.expensetrackerapp.components.LoadingAnimation
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
        AIAnalysisScreen(
            response = response,
        )
    }
}

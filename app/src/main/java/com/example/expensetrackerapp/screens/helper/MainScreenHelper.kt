package com.example.expensetrackerapp.screens.helper

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.expensetrackerapp.components.appbar.AppScreen
import com.example.expensetrackerapp.data.getToken
import com.example.expensetrackerapp.data.viewmodels.AuthenticationViewModel
import com.example.expensetrackerapp.screens.MainScreen

@Composable
fun MainScreenHelper(
    navController: NavHostController,
    context: Context
) {
    val authenticationViewModel: AuthenticationViewModel = viewModel()
    MainScreen(
        context = context,
        validateToken = suspend {
            val token = getToken(context)
            if (token != null) {
                authenticationViewModel.setToken(token)
            }
            authenticationViewModel.validateToken(context)
            authenticationViewModel.isTokenValid()
        },
        goToHome = {
            navController.navigate(AppScreen.Home.route) {
                popUpTo(AppScreen.Register.route) {
                    saveState = true
                    inclusive = true
                }
                launchSingleTop = true
                restoreState = true
            }
        },
        goToLogin = {
            navController.navigate(AppScreen.Login.route) {
                popUpTo(0) {
                    inclusive = true
                }
                launchSingleTop = true
            }
        }
    )
}
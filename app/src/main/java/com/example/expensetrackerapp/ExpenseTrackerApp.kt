package com.example.expensetrackerapp

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.expensetrackerapp.data.getToken

@Composable
fun ExpenseTrackerApp(
    appViewModel: ExpenseTrackerViewModel = viewModel()
) {
    val appUiState by appViewModel.uiState.collectAsState()
    val context = LocalContext.current
    MainScreen(
        context = context,
        loginOnClick = { username, password ->
            appViewModel.setUsernamePassword(username, password)
            appViewModel.performLogin(
                context = context,
//                username = appUiState.username,
//                password = appUiState.password,
                onSuccess = { token ->
                    println("Token: $token")
                    val sharedToken = getToken(context)
                    println("Shared Pref: $sharedToken")
                },
                onError = { error ->
                    println("Error: $error")
                }
            )
        }
    )
}

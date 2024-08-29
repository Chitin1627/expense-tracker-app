package com.example.expensetrackerapp.screens.helper

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.expensetrackerapp.components.appbar.AppScreen
import com.example.expensetrackerapp.data.saveUsername
import com.example.expensetrackerapp.data.viewmodels.AuthenticationViewModel
import com.example.expensetrackerapp.screens.LoginScreen
import com.example.expensetrackerapp.screens.RegisterScreen
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Composable
fun RegisterScreenHelper(
    navController: NavHostController,
    context: Context
) {
    val (isLoading, setLoading) = remember { mutableStateOf(false) }
    val (errorMessage, setErrorMessage) = remember { mutableStateOf<String?>(null) }
    val authenticationViewModel: AuthenticationViewModel = viewModel()

    RegisterScreen(isLoading = isLoading,
        errorMessage = errorMessage,
        registerOnClick = { username, password, email ->
            setLoading(true)
            setErrorMessage(null)

            authenticationViewModel.setUsernamePassword(username, password)
            authenticationViewModel.setEmail(email);

            CoroutineScope(Dispatchers.IO).launch {
                authenticationViewModel.performRegister(
                    context = context,
                    onSuccess = {
                        saveUsername(context, username)
                        navController.navigate(AppScreen.Home.route) {
                            popUpTo(AppScreen.Home.route) {
                                inclusive = true
                            }
                            launchSingleTop = true
                        }
                        setLoading(false)
                    },
                    onError = { error ->
                        setErrorMessage("Server Issue. Please try again later")
                        setLoading(false)
                    }
                )
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
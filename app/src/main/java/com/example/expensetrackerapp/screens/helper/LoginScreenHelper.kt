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
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Composable
fun LoginScreenHelper(
    navController: NavHostController,
    context: Context
) {
    val (isLoading, setLoading) = remember { mutableStateOf(false) }
    val (errorMessage, setErrorMessage) = remember { mutableStateOf<String?>(null) }
    val authenticationViewModel: AuthenticationViewModel = viewModel()

    LoginScreen(
        isLoading = isLoading,
        errorMessage = errorMessage,
        loginOnClick = { username, password ->
            setLoading(true)
            setErrorMessage(null)

            authenticationViewModel.setUsernamePassword(username, password)

            CoroutineScope(Dispatchers.IO).launch {
                authenticationViewModel.performLogin(
                    context = context,
                    onSuccess = {
                        saveUsername(context, username)
                        navController.navigate(AppScreen.Home.route) {
                            popUpTo(AppScreen.Login.route) {
                                inclusive = true
                            }
                            launchSingleTop = true
                        }
                        setLoading(false)
                    },
                    onError = { error ->
                        if(error=="HTTP 401 ") {
                            setErrorMessage("Username or password is wrong")
                        }
                        else {
                            setErrorMessage("Server Issue. Please try again later")
                        }
                        setLoading(false)
                    }
                )
            }
        },
        goToRegister = {
            navController.navigate(AppScreen.Register.route) {
                popUpTo(0) {
                    inclusive = true
                }
                launchSingleTop = true
            }
        }
    )
}
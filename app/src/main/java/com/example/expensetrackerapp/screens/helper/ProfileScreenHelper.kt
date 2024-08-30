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
import com.example.expensetrackerapp.data.viewmodels.AuthenticationViewModel
import com.example.expensetrackerapp.data.viewmodels.HomeScreenViewModel
import com.example.expensetrackerapp.model.PasswordChangeRequest
import com.example.expensetrackerapp.model.UserDetails
import com.example.expensetrackerapp.screens.LoadingScreen
import com.example.expensetrackerapp.screens.ProfileScreen
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Composable
fun ProfileScreenHelper(
    navController: NavHostController,
    homeScreenViewModel: HomeScreenViewModel,
    context: Context
) {
    val authenticationViewModel: AuthenticationViewModel = viewModel()
    var isLoading by remember { mutableStateOf(true) }
    LaunchedEffect(Unit) {
        if(!authenticationViewModel.getIsDataLoaded()) {
            val userDetails = authenticationViewModel.getUserDetailsFromApi(context)
        }
        isLoading = false
    }

    if(isLoading) {
        LoadingScreen()
    }
    else {
        ProfileScreen(
            username = authenticationViewModel.getUsername(),
            email = authenticationViewModel.getEmail(),
            changePassword = {oldPassword,newPassword ->
                val passwordChangeRequest = PasswordChangeRequest(
                    oldPassword = oldPassword,
                    newPassword = newPassword
                )
                authenticationViewModel.changePassword(context, passwordChangeRequest)
            },
            logoutOnClick = {
                removeToken(context = context)
                removeUsername(context = context)
                homeScreenViewModel.clearState()
                navController.navigate(AppScreen.Login.route) {
                    popUpTo(0) {
                        inclusive = true
                    }
                    launchSingleTop = true
                }
            }
        )
    }
}


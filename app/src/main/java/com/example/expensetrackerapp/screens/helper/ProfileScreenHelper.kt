package com.example.expensetrackerapp.screens.helper

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import com.example.expensetrackerapp.components.appbar.AppScreen
import com.example.expensetrackerapp.data.removeToken
import com.example.expensetrackerapp.data.removeUsername
import com.example.expensetrackerapp.data.viewmodels.HomeScreenViewModel
import com.example.expensetrackerapp.screens.ProfileScreen

@Composable
fun ProfileScreenHelper(
    navController: NavHostController,
    homeScreenViewModel: HomeScreenViewModel,
    context: Context
) {
    ProfileScreen(
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
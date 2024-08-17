package com.example.expensetrackerapp.screens

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.example.expensetrackerapp.data.getToken
import com.example.expensetrackerapp.data.removeToken
import kotlinx.coroutines.delay

@Composable
fun MainScreen(
    context: Context,
    loginOnClick: (String, String) -> Unit,
    validateToken: suspend () -> Boolean,
    goToHome: () -> Unit
) {
    //removeToken(context)
    var isTokenValid by remember { mutableStateOf<Boolean?>(null) }
    LaunchedEffect(Unit) {
        val token = getToken(context)
        println("Current token: $token")
        isTokenValid = if (token != null) {
            try {
                validateToken()
            } catch (e: Exception) {
                println("mainscreen: ${e.message}")
                false
            }
        } else {
            false
        }
    }
    println("IsTokenValid: $isTokenValid")
    when (isTokenValid) {
        true -> goToHome()
        false -> LoginScreen(loginOnClick = loginOnClick)
        null -> LoadingScreen()
    }
}
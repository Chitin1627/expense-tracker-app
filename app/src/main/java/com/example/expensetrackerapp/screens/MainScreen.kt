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
    validateToken: suspend () -> Boolean,
    goToHome: () -> Unit,
    goToLogin: () -> Unit
) {
    //removeToken(context)
    var isTokenValid by remember { mutableStateOf<Boolean?>(null) }
    LaunchedEffect(Unit) {
        val token = getToken(context)
        isTokenValid = if (token != null) {
            try {
                validateToken()
            } catch (e: Exception) {
                false
            }
        } else {
            false
        }
    }
    when (isTokenValid) {
        true -> goToHome()
        false -> goToLogin()
        null -> LoadingScreen()
    }
}
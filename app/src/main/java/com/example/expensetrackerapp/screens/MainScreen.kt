package com.example.expensetrackerapp.screens

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import com.example.expensetrackerapp.Greeting
import com.example.expensetrackerapp.data.getToken
import com.example.expensetrackerapp.data.removeToken
import kotlinx.coroutines.launch

@Composable
fun MainScreen(
    context: Context,
    loginOnClick: (String, String) -> Unit,
    validateToken: () -> Boolean
) {
    //removeToken(context)
    var isTokenValid by remember { mutableStateOf<Boolean?>(null) }
    val scope = rememberCoroutineScope()
    LaunchedEffect(Unit) {
        scope.launch {
            val token = getToken(context)
            println("Current token: $token")
            isTokenValid = if (token != null) {
                val isValid = validateToken()
                isValid
            } else {
                false
            }
        }
    }
    println(isTokenValid)
    when (isTokenValid) {
        true -> Greeting("Pra")
        false -> LoginScreen(loginOnClick = loginOnClick)
        null -> LoadingScreen()
    }
}
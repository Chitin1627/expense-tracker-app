package com.example.expensetrackerapp.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import com.example.expensetrackerapp.api.RetrofitClient
import com.example.expensetrackerapp.model.Expense
import com.example.expensetrackerapp.model.LoginRequest
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Response

@Composable
fun LoginScreen() {
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        OutlinedTextField(
            value = username,
            onValueChange = {
                username = it
            }
        )
        Spacer(modifier = Modifier.padding(4.dp))
        OutlinedTextField(
            value = password,
            onValueChange = {
                password = it
            },
            visualTransformation = PasswordVisualTransformation()
        )
        Button(onClick = {
            RetrofitClient.setUsernamePassword(username, password)
            RetrofitClient.createRetrofit()
            val isValid = fetchLoginResponse(
                username = username,
                password = password,
                onSuccess = { token ->
                    // Handle the fetched expenses here
                    println("Expenses: $token")
                },
                onError = { error ->
                    // Handle any errors here
                    println("Error: ${error.message}")
                }
            )
        }) {
            Text(text = "LOGIN")
        }
    }
}


fun fetchLoginResponse(username:String,
                       password:String,
                       onSuccess: (String) -> Unit,
                       onError: (Throwable) -> Unit
) {
    CoroutineScope(Dispatchers.IO).launch {
        try {
            val loginRequest: LoginRequest = LoginRequest(username, password)
            val token = RetrofitClient.authApi.login(loginRequest)
            CoroutineScope(Dispatchers.Main).launch {
                onSuccess(token)
            }
        } catch (e: Exception) {
            onError(e)
        }
    }
}

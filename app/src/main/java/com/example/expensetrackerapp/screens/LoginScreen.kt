package com.example.expensetrackerapp.screens

import android.content.Context
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import com.example.expensetrackerapp.api.RetrofitClient
import com.example.expensetrackerapp.data.getToken
import com.example.expensetrackerapp.data.saveToken
import com.example.expensetrackerapp.model.Expense
import com.example.expensetrackerapp.model.LoginRequest
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

@Composable
fun LoginScreen() {
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    val context = LocalContext.current
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
            val isValid = performLogin(
                context = context,
                username = username,
                password = password,
                onSuccess = { token ->
                    println("Token: $token")
                    val sharedToken = getToken(context)
                    println("Shared Pref: $sharedToken")
                },
                onError = { error ->
                    println("Error: $error")
                }
            )
        }) {
            Text(text = "LOGIN")
        }
    }
}


//fun fetchLoginResponse(username:String,
//                       password:String,
//                       context: Context,
//                       onSuccess: (String) -> Unit,
//                       onError: (Throwable) -> Unit
//) {
//    CoroutineScope(Dispatchers.IO).launch {
//        try {
//            val loginRequest: LoginRequest = LoginRequest(username, password)
//            val token = RetrofitClient(context).authApi.login(loginRequest)
//            CoroutineScope(Dispatchers.Main).launch {
//                onSuccess(token)
//            }
//        } catch (e: Exception) {
//            onError(e)
//        }
//    }
//}

fun performLogin(context: Context, username: String, password: String, onSuccess: (String) -> Unit, onError: (String) -> Unit) {
    val retrofitClient = RetrofitClient(context)
    val api = retrofitClient.authApi

    // Launch a coroutine on IO dispatcher for background work
    CoroutineScope(Dispatchers.IO).launch {
        try {
            // Make the API call and get the token
            val token = api.login(LoginRequest(username, password))

            // Save the token locally
            saveToken(context, token)

            // Switch to the Main thread to handle UI-related tasks
            withContext(Dispatchers.Main) {
                onSuccess(token)
            }
        } catch (e: Exception) {
            // Handle the error, switch to Main thread for UI-related tasks
            withContext(Dispatchers.Main) {
                onError(e.message ?: "An error occurred")
            }
        }
    }
}

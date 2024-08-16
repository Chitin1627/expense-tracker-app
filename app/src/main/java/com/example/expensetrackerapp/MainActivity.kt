package com.example.expensetrackerapp

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.expensetrackerapp.api.RetrofitClient
import com.example.expensetrackerapp.data.getToken
import com.example.expensetrackerapp.data.removeToken
import com.example.expensetrackerapp.data.saveToken
import com.example.expensetrackerapp.model.Expense
import com.example.expensetrackerapp.model.JwtToken
import com.example.expensetrackerapp.screens.LoginScreen
import com.example.expensetrackerapp.ui.theme.ExpenseTrackerAppTheme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ExpenseTrackerAppTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    ExpenseTrackerApp()
                }
            }
        }
    }
}

@Composable
fun MainScreen(
    context: Context,
    loginOnClick: (String, String) -> Unit
) {
    // State to track whether token is valid or not
    var isTokenValid by remember { mutableStateOf<Boolean?>(null) }

    // Coroutine scope for background work
    val scope = rememberCoroutineScope()
    // Validate the token when the composable is first displayed
    LaunchedEffect(Unit) {
        scope.launch {
            val token = getToken(context)
            println("Current token: $token")
            isTokenValid = if (token != null) {
                // Check token validity
                val isValid = validateToken(context, token)
                isValid
            } else {
                false
            }
        }
    }
    println(isTokenValid)
    when (isTokenValid) {
        true -> Greeting("Pra")
        else -> LoginScreen(loginOnClick = loginOnClick)
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

suspend fun validateToken(context: Context, token: String): Boolean {
    val isValid = try {
        val retrofitClient = RetrofitClient(context)
        val api = retrofitClient.authApi

        // Call your validateToken API endpoint
        val response = api.validateToken(JwtToken(token))
        println("isValid: $response")
        true
    } catch (e: Exception) {
        println(e.message)
        false // Token validation failed, consider it invalid
    }
    return isValid
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    ExpenseTrackerAppTheme {
        Greeting("Android")
    }
}
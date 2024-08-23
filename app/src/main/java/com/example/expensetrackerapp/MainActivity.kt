package com.example.expensetrackerapp

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.view.WindowCompat
import com.example.expensetrackerapp.api.RetrofitClient
import com.example.expensetrackerapp.model.JwtToken
import com.example.expensetrackerapp.screens.PieChartScreen
import com.example.expensetrackerapp.ui.theme.ExpenseTrackerAppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ExpenseTrackerAppTheme {
                setStatusBarColor(
                    isDarkTheme = isSystemInDarkTheme()
                )
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    ExpenseTrackerApp()
                }
            }
        }
    }

    private fun setStatusBarColor(isDarkTheme: Boolean) {
        WindowCompat.getInsetsController(window, window.decorView).apply {
            isAppearanceLightStatusBars = !isDarkTheme
        }
        window.statusBarColor = if (isDarkTheme) {
            Color.Black.toArgb()
        } else {
            Color.White.toArgb()
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    ExpenseTrackerAppTheme {
        Greeting("Android")
    }
}
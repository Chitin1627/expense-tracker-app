package com.example.expensetrackerapp.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

// Define the light color scheme
private val LightColorScheme = lightColorScheme(
    primary = GreenLight,
    onPrimary = White,
    background = White,
    onBackground = BlackLight,
    surface = BlackLight,
    onSurface = BlackLight
)

// Define the dark color scheme
private val DarkColorScheme = darkColorScheme(
    primary = GreenDark,
    onPrimary = BlackDark,
    background = BlackDark,
    onBackground = White,
    surface = BlackDark,
    onSurface = White
)

@Composable
fun ExpenseTrackerAppTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography, // Default typography or customize it
        content = content
    )
}

package com.example.expensetrackerapp.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorPalette = darkColorScheme(
    primary = Green,
    onPrimary = Color.White,
    secondary = LightGreen,
    onSecondary = Color.Black,
    background = Black,
    onBackground = Color.White,
    surface = DarkGray,
    onSurface = Color.White
)

private val LightColorPalette = lightColorScheme(
    primary = Green,
    onPrimary = Color.Black,
    secondary = LightGreen,
    onSecondary = Black,
    background = Color.White,
    onBackground = Black,
    surface = Color.White,
    onSurface = Black
)

@Composable
fun ExpenseTrackerAppTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    val colors = if (darkTheme) {
        DarkColorPalette
    } else {
        LightColorPalette
    }

    MaterialTheme(
        colorScheme = colors,
        typography = Typography,
        content = content
    )
}
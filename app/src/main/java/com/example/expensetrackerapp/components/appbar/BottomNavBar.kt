package com.example.expensetrackerapp.components.appbar

import androidx.compose.material3.NavigationBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

val items = listOf(
    AppScreen.Statistics,
    AppScreen.Home,
    AppScreen.AddExpense,
    AppScreen.Profile
)
@Composable
fun BottomNavBar(
    navigateTo: (String) -> Unit,
    currentRoute: String?
) {
    NavigationBar(
        containerColor = Color.Transparent
    ) {
        items.forEach { item ->
            AddItem(
                screen = item,
                currentRoute = currentRoute,
                onClick = {navigateTo(item.route)}
            )
        }
    }
}

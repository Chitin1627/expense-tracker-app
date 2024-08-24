package com.example.expensetrackerapp.components.appbar

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AddCircle
import androidx.compose.material.icons.outlined.Check
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Refresh
import androidx.compose.ui.graphics.vector.ImageVector

sealed class AppScreen(
    var title: String,
    var icon: ImageVector,
    val route: String
) {
    object Home :
        AppScreen(
            "Home",
            Icons.Outlined.Home,
            "home"
        )

    object Statistics :
        AppScreen(
            "Statistics",
            Icons.Outlined.CheckCircle,
            "statistics"
        )

    object AddExpense:
            AppScreen(
                "Add Expense",
                Icons.Outlined.AddCircle,
                "add Expense"
            )


    object Profile :
        AppScreen(
            "Profile",
            Icons.Outlined.Person,
            "profile"
        )

    object Login :
        AppScreen(
            "Login",
            Icons.Outlined.Check,
            "login"
        )
    object Loading :
        AppScreen(
            "Loading",
            Icons.Outlined.Refresh,
            "loading"
        )

    object Validating:
            AppScreen(
                "Validating",
                Icons.Outlined.Check,
                "validating"
            )

}
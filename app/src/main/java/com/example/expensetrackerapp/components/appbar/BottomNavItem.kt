package com.example.expensetrackerapp.components.appbar

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Check
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Refresh
import androidx.compose.ui.graphics.vector.ImageVector

sealed class BottomNavItem(
    var title: String,
    var icon: ImageVector,
    val route: String
) {
    object Home :
        BottomNavItem(
            "Home",
            Icons.Outlined.Home,
            "home"
        )

    object Statistics :
        BottomNavItem(
            "Statistics",
            Icons.Outlined.CheckCircle,
            "statistics"
        )


    object Profile :
        BottomNavItem(
            "Profile",
            Icons.Outlined.Person,
            "profile"
        )

    object Login :
        BottomNavItem(
            "Login",
            Icons.Outlined.Check,
            "login"
        )
    object Loading :
        BottomNavItem(
            "Loading",
            Icons.Outlined.Refresh,
            "loading"
        )

    object Validating:
            BottomNavItem(
                "Validating",
                Icons.Outlined.Check,
                "validating"
            )

}
package com.example.expensetrackerapp.components

import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.navigation.NavBackStackEntry

fun defaultEnterTransition(): (AnimatedContentTransitionScope<NavBackStackEntry>.() -> EnterTransition?)? {
    return {
        slideInHorizontally(
            initialOffsetX = { 1000 },
            animationSpec = tween(durationMillis = 500)
        ) + fadeIn(animationSpec = tween(durationMillis = 500))
    }
}

fun defaultExitTransition(): (AnimatedContentTransitionScope<NavBackStackEntry>.() -> ExitTransition?)? {
    return {
        slideOutHorizontally(
            targetOffsetX = { -1000 },
            animationSpec = tween(durationMillis = 500)
        ) + fadeOut(animationSpec = tween(durationMillis = 500))
    }
}

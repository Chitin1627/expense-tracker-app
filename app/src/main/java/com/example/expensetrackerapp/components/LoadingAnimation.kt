package com.example.expensetrackerapp.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import kotlinx.coroutines.delay

@Composable
fun LoadingAnimation() {
    val sentences = listOf(
        "Hacking mainframe",
        "Stealing cookies",
        "Decrypting memes",
        "Summoning demons",
        "Charging sarcasm",
        "Warming up lasers",
        "Compiling bad jokes",
        "Enhancing pixels"
    )

    val randomSentence by remember { mutableStateOf(sentences.random()) }
    var dotCount by remember { mutableStateOf(0) }

    LaunchedEffect(Unit) {
        while (true) {
            delay(500L)
            dotCount = (dotCount + 1) % 4
        }
    }

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = randomSentence + ".".repeat(dotCount),
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary
        )
    }

}

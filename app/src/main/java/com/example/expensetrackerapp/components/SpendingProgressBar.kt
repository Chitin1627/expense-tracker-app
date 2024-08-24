package com.example.expensetrackerapp.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
fun SpendingProgressBar(
    totalSpent: Double,
    monthlyLimit: Double,
    modifier: Modifier
) {
    val progress = if (monthlyLimit > 0) (totalSpent / monthlyLimit).toFloat() else 0f

    Column {
        Text(
            text = "Rs.$totalSpent/$monthlyLimit",
            color = if(progress<1f) MaterialTheme.colorScheme.primary else Color.Red,
            style = MaterialTheme.typography.titleSmall,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .padding(bottom = 2.dp)
        )
        Row(
            modifier = modifier
                .fillMaxSize()
                .border(4.dp, if (progress < 1f) MaterialTheme.colorScheme.primary else Color.Red)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth(fraction = progress.coerceIn(0f, 1f))
                    .fillMaxHeight()
                    .background(if (progress < 1f) MaterialTheme.colorScheme.primary else Color.Red)
                    .clip(RoundedCornerShape(16.dp))
            )
            Box(
                modifier = Modifier
                    .fillMaxWidth(fraction = (1f - progress).coerceIn(0f, 1f))
                    .fillMaxHeight()
                    .background(if (progress < 1f) MaterialTheme.colorScheme.background else Color.Red)
                    .clip(RoundedCornerShape(16.dp))
            )
        }
    }
}

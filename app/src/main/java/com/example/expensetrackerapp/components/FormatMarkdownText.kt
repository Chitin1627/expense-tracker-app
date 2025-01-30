package com.example.expensetrackerapp.components

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight

@Composable
fun formatMarkdownText(text: String): AnnotatedString {
    val regex = Regex("\\*\\*(.*?)\\*\\*")
    val annotatedString = buildAnnotatedString {
        var lastIndex = 0
        regex.findAll(text).forEach { matchResult ->
            val start = matchResult.range.first
            val end = matchResult.range.last + 1
            val boldText = matchResult.groupValues[1]

            append(text.substring(lastIndex, start))

            pushStyle(SpanStyle(color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.Bold))
            append(boldText)
            pop()

            lastIndex = end + 1
        }

        append(text.substring(lastIndex))
    }
    return annotatedString
}

package com.example.expensetrackerapp.components

import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight

fun formatMarkdownText(text: String): AnnotatedString {
    val regex = Regex("\\*\\*(.*?)\\*\\*")
    val annotatedString = buildAnnotatedString {
        var lastIndex = 0
        regex.findAll(text).forEach { matchResult ->
            val start = matchResult.range.first
            val end = matchResult.range.last + 1
            val boldText = matchResult.groupValues[1] // Extract text inside ** **

            append(text.substring(lastIndex, start))

            pushStyle(SpanStyle(fontWeight = FontWeight.Bold))
            append(boldText)
            pop()

            lastIndex = end + 1
        }

        append(text.substring(lastIndex))
    }
    return annotatedString
}

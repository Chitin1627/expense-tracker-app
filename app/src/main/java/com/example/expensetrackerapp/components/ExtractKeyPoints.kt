package com.example.expensetrackerapp.components

fun extractKeyPoints(response: String): List<String> {
    val regex = Regex("\\d+\\.\\s\\*\\*(.*?)\\*\\*") // Matches "1. **Title**"
    val matches = regex.findAll(response)

    return matches.map { matchResult ->
        val title = matchResult.groupValues[1]
        val startIndex = matchResult.range.last + 1
        val nextMatch = matches.find { it.range.first > startIndex }

        val endIndex = nextMatch?.range?.first ?: response.length
        val description = response.substring(startIndex, endIndex).trim()

        "**$title**\n\n$description"
    }.toList()
}

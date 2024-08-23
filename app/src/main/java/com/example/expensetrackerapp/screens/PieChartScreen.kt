package com.example.expensetrackerapp.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.expensetrackerapp.components.PieChart
import com.example.expensetrackerapp.model.CategoryExpense

@Composable
fun PieChartScreen(
    pieChartData: List<CategoryExpense>
) {
    PieChart(pieChartData)
}

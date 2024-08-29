package com.example.expensetrackerapp.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.expensetrackerapp.components.ExpenseCard
import com.example.expensetrackerapp.model.Expense
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.text.font.FontWeight
import java.text.SimpleDateFormat
import java.util.Locale

@Composable
fun ExpenseByDateScreen(
    date: String,
    expenses: List<Expense>,
    categories: HashMap<String, String>
) {
    val inputFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    val outputFormat = SimpleDateFormat("dd MMM yyyy", Locale.getDefault())

    var expandedCardId by remember { mutableStateOf<String?>(null) }

    if(expenses.isNotEmpty()) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            Text(
                text = outputFormat.format(inputFormat.parse(date)?:""),
                style = MaterialTheme.typography.headlineLarge,
                color = MaterialTheme.colorScheme.onBackground,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(start = 24.dp)
            )

            Text(
                text = "Click a card see the description",
                style = MaterialTheme.typography.titleSmall,
                color = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier.padding(start = 24.dp)
            )

            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(expenses) { expense ->
                    val category = categories[expense.category_id]
                    if (category != null) {
                        ExpenseCard(
                            expense = expense,
                            category = category,
                            isExpanded = expandedCardId == expense._id,
                            onCardClick = { expenseId ->
                                expandedCardId = if (expandedCardId == expenseId) null else expenseId
                            }
                        )
                    }
                }
            }
        }
    }
    else {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Nothing to see here :(",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.primary
            )
        }
    }

}
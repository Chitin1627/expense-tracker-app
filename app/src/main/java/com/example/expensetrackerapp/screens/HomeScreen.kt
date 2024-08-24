package com.example.expensetrackerapp.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ShapeDefaults
import androidx.compose.material3.Shapes
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.expensetrackerapp.components.MonthlyLimitDialog
import com.example.expensetrackerapp.components.SpendingProgressBar
import com.example.expensetrackerapp.data.getUsername
import com.example.expensetrackerapp.model.CategoryExpense
import com.example.expensetrackerapp.model.Expense
import kotlin.math.exp

@Composable
fun HomeScreen(
    expenses: List<Expense>,
    categories: HashMap<String, String>,
    expenseByCategory: List<CategoryExpense>,
    monthlyLimit: Double,
    currentMonthExpense: Double,
    onSetLimit: (Double) -> Unit
) {
//    LazyColumn(
//        modifier = Modifier
//            .fillMaxSize()
//            .padding(16.dp),
//        verticalArrangement = Arrangement.spacedBy(8.dp)
//    ) {
//        items(expenses) { expense ->
//            val category = categories[expense.category_id]
//            if (category != null) {
//                ExpenseCard(expense, category)
//            }
//        }
//    }
    val context = LocalContext.current
    var showDialog by rememberSaveable {
        mutableStateOf(false)
    }
    var monthlyExpenseLimit by rememberSaveable {
        mutableStateOf(monthlyLimit)
    }

    Column(modifier = Modifier.fillMaxSize()) {
        Row {
            Text(
                text = "Hello ",
                color = MaterialTheme.colorScheme.onBackground,
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(start = 8.dp)
            )
            Text(
                text = "${getUsername(context)},",
                color = MaterialTheme.colorScheme.primary,
                style = MaterialTheme.typography.titleMedium,
            )
        }
        Box(modifier = Modifier
            .fillMaxSize()
            .weight(3f)
        ) {
            PieChartScreen(pieChartData = expenseByCategory)
        }
        Box(modifier = Modifier
            .fillMaxSize()
            .weight(1f)
            .padding(8.dp)
        ) {
            Row(modifier = Modifier
                .fillMaxSize(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Button(
                    onClick = { /*TODO*/ },
                    modifier = Modifier
                        .fillMaxSize()
                        .weight(1f)
                        .padding(start = 8.dp, end = 4.dp, top = 4.dp, bottom = 8.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
                    border = BorderStroke(2.dp, Color.Black)
                ) {
                    Text(text = "Button1")
                }
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .weight(1f)
                        .padding(start = 16.dp, end = 8.dp, bottom = 8.dp),
                    verticalArrangement = Arrangement.SpaceEvenly
                ) {
                    Column(
                        modifier = Modifier.weight(1f)
                    ) {
                        SpendingProgressBar(
                            totalSpent = currentMonthExpense,
                            monthlyLimit = monthlyExpenseLimit,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(start = 2.dp, top = 4.dp, bottom = 8.dp)
                                .clip(RoundedCornerShape(8.dp))
                        )
                    }
                    Button(
                        onClick = { showDialog = true },
                        modifier = Modifier
                            .fillMaxSize()
                            .weight(1f),
                        shape = RoundedCornerShape(16.dp),
                        border = BorderStroke(2.dp, Color.Black)
                    ) {
                        Text(text = "Set Monthly Limit")
                    }
                }
            }
        }
    }

    if(showDialog) {
        MonthlyLimitDialog(
            onSetLimit = {limit ->
                onSetLimit(limit)
                monthlyExpenseLimit = limit
            },
            onDismiss = {showDialog = false}
        )
    }
}

@Composable
fun ExpenseCard(
    expense: Expense,
    category: String
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        elevation = CardDefaults.cardElevation(4.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.background),
        border = BorderStroke(2.dp, color = MaterialTheme.colorScheme.primary)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(
                text = "Amount: ${expense.amount}",
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Start
            )
            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = "Category: $category",
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Start
            )
            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = "Date: ${expense.date}",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.secondary,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Start
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = expense.description,
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Start
            )
        }
    }
}
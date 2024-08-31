package com.example.expensetrackerapp.screens

import android.app.Activity
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
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
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import com.example.expensetrackerapp.components.AutoResizedText
import com.example.expensetrackerapp.components.DateSelectorButton
import com.example.expensetrackerapp.components.MonthlyLimitDialog
import com.example.expensetrackerapp.components.SpendingProgressBar
import com.example.expensetrackerapp.data.getUsername
import com.example.expensetrackerapp.model.CategoryExpense
import com.example.expensetrackerapp.model.Expense
import kotlin.math.exp

@Composable
fun HomeScreen(
    expenseByCategory: List<CategoryExpense>,
    monthlyLimit: Double,
    currentMonthExpense: Double,
    onSetLimit: (Double) -> Unit,
    onDateSelected: (String) -> Unit,
) {
    val context = LocalContext.current
    BackHandler {
        (context as? Activity)?.finish()
    }

    var showDialog by rememberSaveable {
        mutableStateOf(false)
    }
    var monthlyExpenseLimit by remember {
        mutableDoubleStateOf(monthlyLimit)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        AutoResizedText(
            text = buildAnnotatedString {
                withStyle(style = SpanStyle(color = MaterialTheme.colorScheme.onBackground)) {
                    append("Hello ")
                }
                withStyle(style = SpanStyle(color = MaterialTheme.colorScheme.primary)) {
                    append("${getUsername(context)?.replaceFirstChar(Char::titlecase)}!")
                }
            },
            modifier = Modifier.padding(16.dp),
            color = MaterialTheme.colorScheme.primary,
            fontWeight = FontWeight.Bold,
            style = MaterialTheme.typography.headlineMedium,
        )

        Box(modifier = Modifier
            .fillMaxSize()
            .weight(5f),
            contentAlignment = Alignment.Center
        ) {
            if(currentMonthExpense==0.0) {
                Column(
                    modifier = Modifier
                        .fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    AutoResizedText(
                        text = AnnotatedString("Guess you are saving a lot ;)"),
                        color = MaterialTheme.colorScheme.onBackground,
                        style = MaterialTheme.typography.titleMedium
                    )
                    Spacer(modifier = Modifier.padding(8.dp))
                    AutoResizedText(
                        text = buildAnnotatedString {
                            withStyle(style = SpanStyle(color = MaterialTheme.colorScheme.onBackground)) {
                                append("You have spent ")
                            }
                            withStyle(style = SpanStyle(color = MaterialTheme.colorScheme.primary)) {
                                append("Rs. 0 ")
                            }
                            withStyle(style = SpanStyle(color = MaterialTheme.colorScheme.onBackground)) {
                                append("this month")
                            }

                        },
                        color = MaterialTheme.colorScheme.onBackground,
                        style = MaterialTheme.typography.titleSmall
                    )
                }
            }
            else {
                PieChartScreen(pieChartData = expenseByCategory)
            }

        }
        Box(modifier = Modifier
            .fillMaxSize()
            .weight(2f)
            .padding(8.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(8.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(2f)
                ) {
                    SpendingProgressBar(
                        totalSpent = currentMonthExpense,
                        monthlyLimit = monthlyExpenseLimit,
                        modifier = Modifier
                            .fillMaxWidth()
                            .fillMaxHeight()
                            .padding(start = 2.dp, top = 4.dp, bottom = 8.dp)
                            .clip(RoundedCornerShape(8.dp))
                    )
                }
                Row(
                    modifier = Modifier
                        .padding(top = 8.dp, bottom = 8.dp)
                        .weight(3.5f)
                        .fillMaxSize(),
                    horizontalArrangement = Arrangement.Center
                ) {
                    DateSelectorButton(
                        onDateSelected = onDateSelected,
                        text = "Expense by Date",
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxSize()
                            .padding(end = 8.dp)
                    )

                    Button(
                        onClick = { showDialog = true },
                        modifier = Modifier
                            .fillMaxSize()
                            .weight(1f)
                            .padding(start = 8.dp),
                        shape = RoundedCornerShape(16.dp),
                        //border = BorderStroke(2.dp, MaterialTheme.colorScheme.onBackground)
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


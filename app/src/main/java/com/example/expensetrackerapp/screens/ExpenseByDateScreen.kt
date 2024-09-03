package com.example.expensetrackerapp.screens

import android.annotation.SuppressLint
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.expensetrackerapp.data.viewmodels.AddExpenseViewModel
import com.example.expensetrackerapp.data.viewmodels.HomeScreenViewModel
import java.text.SimpleDateFormat
import java.util.Locale

@SuppressLint("MutableCollectionMutableState")
@Composable
fun ExpenseByDateScreen(
    date: String,
    expenses: ArrayList<Expense>,
    homeScreenViewModel: HomeScreenViewModel,
    categories: HashMap<String, String>,
    onExpenseDelete: (String) -> Unit
) {
    val context = LocalContext.current

    val inputFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    val outputFormat = SimpleDateFormat("dd MMM yyyy", Locale.getDefault())
    val addExpenseViewModel: AddExpenseViewModel = viewModel()

    val deletedExpense by remember { mutableStateOf(HashMap<String, Boolean>()) }
    var expandedCardId by remember { mutableStateOf<String?>(null) }
    var editingCardId by remember { mutableStateOf<String?>(null) }
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        Text(
            text = outputFormat.format(inputFormat.parse(date) ?: ""),
            style = MaterialTheme.typography.headlineLarge,
            color = MaterialTheme.colorScheme.onBackground,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(start = 24.dp)
        )

        Text(
            text = "Click on an expense to see the description",
            style = MaterialTheme.typography.titleSmall,
            color = MaterialTheme.colorScheme.onBackground,
            modifier = Modifier.padding(start = 24.dp)
        )

        if (expenses.isNotEmpty()) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(expenses) { expense ->
                    if(!deletedExpense.containsKey(expense._id)) {
                        val category = categories[expense.category_id]
                        if (category != null) {
                            ExpenseCard(
                                expense = expense,
                                category = category,
                                isExpanded = expandedCardId == expense._id,
                                isEditing = editingCardId == expense._id,
                                isClickable = editingCardId==null,
                                onEditClick = {expenseId ->
                                    editingCardId = expenseId
                                },
                                onCancelClick = {
                                    editingCardId = null
                                },
                                onCardClick = { expenseId ->
                                    expandedCardId =
                                        if (expandedCardId == expenseId) null else expenseId
                                },
                                onDeleteClick = { expenseId ->
                                    onExpenseDelete(expenseId)
                                    deletedExpense[expenseId] = true
                                },
                                onSave = { expenseId, amount, categoryId, type, description, date, created_at ->
                                    addExpenseViewModel.setExpenseId(expenseId)
                                    addExpenseViewModel.setAmount(amount)
                                    addExpenseViewModel.setCategory(categoryId)
                                    addExpenseViewModel.setDescription(description)
                                    addExpenseViewModel.setDate(date)
                                    addExpenseViewModel.setType(type)
                                    addExpenseViewModel.setCreatedAt(created_at)
                                    try {
                                        val success = addExpenseViewModel.editExpense(context)
                                        if (success) {
                                            val index = expenses.indexOfFirst { it._id == expenseId }
                                            if (index != -1) {
                                                expenses[index] = expenses[index].copy(
                                                    amount = amount,
                                                    category_id = categoryId,
                                                    type = type,
                                                    description = description,
                                                    date = date,
                                                    created_at = created_at
                                                )
                                            }
                                            homeScreenViewModel.setIsDataLoaded(false)
                                        }
                                        success
                                    } catch (e: Exception) {
                                        false
                                    }
                                },
                                categoryNameMap = homeScreenViewModel.getCategoriesNameMap()
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
}
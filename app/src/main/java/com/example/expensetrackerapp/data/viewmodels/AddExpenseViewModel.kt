package com.example.expensetrackerapp.data.viewmodels

import android.content.Context
import androidx.lifecycle.ViewModel
import com.example.expensetrackerapp.api.RetrofitClient
import com.example.expensetrackerapp.data.getUsername
import com.example.expensetrackerapp.data.uistates.AddExpenseUiState
import com.example.expensetrackerapp.model.Expense
import com.example.expensetrackerapp.model.ExpenseRequest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.withContext

class AddExpenseViewModel: ViewModel() {
    private val _uiState = MutableStateFlow(AddExpenseUiState())
    val uiState: StateFlow<AddExpenseUiState> = _uiState.asStateFlow()

    fun setAmount(amount: Double) {
        _uiState.update { currentState ->
            currentState.copy(
                amount = amount
            )
        }
    }

    fun getAmount(): Double {
        return uiState.value.amount
    }

    fun setExpenseId(expenseId: String) {
        _uiState.update { currentState ->
            currentState.copy(
                expenseId = expenseId
            )
        }
    }

    fun getExpenseId(): String {
        return uiState.value.expenseId
    }

    fun setCategory(category: String) {
        _uiState.update { currentState ->
            currentState.copy(
                category = category
            )
        }
    }

    fun getCategory(): String {
        return uiState.value.category
    }

    fun setDescription(description: String) {
        _uiState.update { currentState ->
            currentState.copy(
                description = description
            )
        }
    }

    fun getDescription(): String {
        return uiState.value.description
    }

    fun setDate(date: String) {
        _uiState.update { currentState ->
            currentState.copy(
                date = date
            )
        }
    }

    fun getDate(): String {
        return uiState.value.date
    }

    fun setCreatedAt(created_at: String) {
        _uiState.update { currentState ->
            currentState.copy(
                created_at = created_at
            )
        }
    }

    fun getCreatedAt(): String {
        return uiState.value.created_at
    }


    fun setType(type: String) {
        _uiState.update { currentState ->
            currentState.copy(
                type = type
            )
        }
    }

    fun getType(): String {
        return uiState.value.type
    }

    suspend fun createExpense(context: Context): Boolean = withContext(Dispatchers.IO) {
        val amount = getAmount()
        val category = getCategory()
        val type = getType()
        val description = getDescription()
        val date = getDate()
        try {
            val retrofitClient = RetrofitClient(context)
            val api = retrofitClient.expenseApi
            val expense = ExpenseRequest(
                amount = amount,
                category_id = category,
                type = type,
                description = description,
                date = date
            )
            val response = api.createExpense(expense)
            response.isSuccessful
        } catch (e: Exception) {
            false
        }
    }

    suspend fun editExpense(context: Context): Boolean = withContext(Dispatchers.IO) {
        val expenseId = getExpenseId()
        val amount = getAmount()
        val category = getCategory()
        val type = getType()
        val description = getDescription()
        val date = getDate()
        val created_at = getCreatedAt()
        try {
            val retrofitClient = RetrofitClient(context)
            val api = retrofitClient.expenseApi
            val expense = Expense(
                _id = expenseId,
                username = "",
                amount = amount,
                category_id = category,
                type = type,
                description = description,
                date = date,
                created_at = created_at
            )
            val response = api.editExpense(expense)
            println(response.body())
            response.isSuccessful
        } catch (e: Exception) {
            println(e.message)
            false
        }
    }
}
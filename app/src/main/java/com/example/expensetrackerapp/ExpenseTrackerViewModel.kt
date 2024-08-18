package com.example.expensetrackerapp

import android.content.Context
import androidx.lifecycle.ViewModel
import com.example.expensetrackerapp.api.RetrofitClient
import com.example.expensetrackerapp.data.ExpenseTrackerUiState
import com.example.expensetrackerapp.data.getUsername
import com.example.expensetrackerapp.data.saveToken
import com.example.expensetrackerapp.model.Category
import com.example.expensetrackerapp.model.Expense
import com.example.expensetrackerapp.model.ExpenseRequest
import com.example.expensetrackerapp.model.JwtToken
import com.example.expensetrackerapp.model.LoginRequest
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.Date

class ExpenseTrackerViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(ExpenseTrackerUiState())
    val uiState: StateFlow<ExpenseTrackerUiState> = _uiState.asStateFlow()

    fun setUsernamePassword(username: String, password: String) {
        _uiState.update {currentState ->
            currentState.copy(
                username = username,
                password = password
            )
        }
    }

    fun setExpenses(expenses: List<Expense>) {
        _uiState.update {currentState ->
            currentState.copy(
                expenses = expenses
            )
        }
    }

    fun getExpenses(): List<Expense> {
        return uiState.value.expenses
    }

    fun setTotalExpense(expenses: List<Expense>) {
        var totalExpense: Double = 0.0
        expenses.forEach { expense ->
            totalExpense+=expense.amount
        }
        _uiState.update { currentState->
            currentState.copy(
                totalExpense = totalExpense
            )
        }
    }

    fun setToken(token: String) {
        _uiState.update { currentState ->
            currentState.copy(
                token = token
            )
        }
    }

    fun getCategories(): HashMap<String, String> {
        return uiState.value.categories
    }

    fun getCategoriesNameMap(): HashMap<String, String> {
        return uiState.value.categoryNameMap
    }

    fun isTokenValid(): Boolean {
        return uiState.value.isTokenValid
    }

    suspend fun performLogin(context: Context, onSuccess: (String) -> Unit, onError: (String) -> Unit) {
        val retrofitClient = RetrofitClient(context)
        val api = retrofitClient.authApi
        val username = uiState.value.username
        val password = uiState.value.password
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val token = api.login(LoginRequest(username, password))
                saveToken(context, token)
                withContext(Dispatchers.Main) {
                    onSuccess(token)
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    onError(e.message ?: "An error occurred")
                }
            }
        }
    }

     suspend fun validateToken(context: Context): Boolean{
         val retrofitClient = RetrofitClient(context)
         val api = retrofitClient.authApi
         val token = uiState.value.token
         return withContext(Dispatchers.IO) {
             try {
                 val response = api.validateToken(JwtToken(token))
                 println("isValid: $response")
                 val isValid = response == "true"
                 withContext(Dispatchers.Main) {
                     _uiState.update { currentState ->
                         currentState.copy(isTokenValid = isValid)
                     }
                 }
                 isValid
             } catch (e: Exception) {
                 println("Error: ${e.message}")
                 withContext(Dispatchers.Main) {
                     _uiState.update { currentState ->
                         currentState.copy(isTokenValid = false)
                     }
                 }
                 false
             }
         }
    }

    suspend fun getExpensesFromApi(context: Context): List<Expense> {
        val retrofitClient = RetrofitClient(context)
        val api = retrofitClient.expenseApi
        return withContext(Dispatchers.IO) {
            try {
                val response = api.getUserExpenses()
                println(response)
                withContext(Dispatchers.Main) {
                    _uiState.update { currentState ->
                        currentState.copy(
                            expenses = response
                        )
                    }
                }
                response
            } catch(e: Exception) {
                println(e.message)
                emptyList()
            }
        }
    }

    suspend fun getCategoriesFromApi(context: Context): List<Category> {
        val retrofitClient = RetrofitClient(context)
        val api = retrofitClient.categoryApi
        return withContext(Dispatchers.IO) {
            try {
                val response = api.getCategories()
                val categoryMap = HashMap<String, String>()
                val categoryNameMap = HashMap<String, String>()
                response.forEach { category ->
                    categoryMap.put(category._id, category.name)
                    categoryNameMap.put(category.name, category._id)
                }
                withContext(Dispatchers.Main) {
                    _uiState.update { currentState ->
                        currentState.copy(
                            categories = categoryMap,
                            categoryNameMap = categoryNameMap
                        )
                    }
                }
                response
            } catch(e: Exception) {
                println(e.message)
                emptyList()
            }
        }
    }

    suspend fun createExpense(context: Context, amount: Double, category: String, description: String, date: String) {
        val retrofitClient = RetrofitClient(context)
        val api = retrofitClient.expenseApi
        withContext(Dispatchers.Main) {
            try {
                val categories = uiState.value.categoryNameMap
                val category_id = categories[category] ?: ""
                val expense = ExpenseRequest(
                    username = getUsername(context) ?: "",
                    amount = amount,
                    category_id = category_id,
                    description = description,
                    date = date
                )
                val response = api.createExpense(expense)
                if (response.isSuccessful) {
                    println("Expense Created")
                } else {
                    println("Error: ${response.code()}")
                }
            } catch (e: Exception) {
                println("Exception: ${e.message}")
            }
        }
    }
}
package com.example.expensetrackerapp

import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import com.example.expensetrackerapp.api.RetrofitClient
import com.example.expensetrackerapp.data.ExpenseTrackerUiState
import com.example.expensetrackerapp.data.getUsername
import com.example.expensetrackerapp.data.saveToken
import com.example.expensetrackerapp.model.Category
import com.example.expensetrackerapp.model.CategoryExpense
import com.example.expensetrackerapp.model.Expense
import com.example.expensetrackerapp.model.ExpenseRequest
import com.example.expensetrackerapp.model.JwtToken
import com.example.expensetrackerapp.model.LoginRequest
import com.example.expensetrackerapp.model.UserSpendingRequest
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.time.LocalDate
import java.time.format.DateTimeFormatter

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
        var totalExpense = 0.0
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
                    categoryMap[category._id] = category.name
                    categoryNameMap[category.name] = category._id
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

    suspend fun createExpense(
        context: Context,
        amount: Double,
        category: String,
        description: String,
        date: String
    ): Boolean = withContext(Dispatchers.IO) {
        try {
            val retrofitClient = RetrofitClient(context)
            val api = retrofitClient.expenseApi
            val expense = ExpenseRequest(
                username = getUsername(context) ?: "",
                amount = amount,
                category_id = category,
                description = description,
                date = date
            )
            val response = api.createExpense(expense)
            response.isSuccessful
        } catch (e: Exception) {
            println("Exception: ${e.message}")
            false
        }
    }


    suspend fun getSpendingLimitFromApi(context: Context) {
        val retrofitClient = RetrofitClient(context)
        val api = retrofitClient.userSpendingApi
        withContext(Dispatchers.IO) {
            val response = api.getUserSpending()
            if(response.username!=null) {
                _uiState.update { currentState->
                    currentState.copy(
                        monthlyLimit = response.monthlyLimit
                    )
                }
            }
        }
    }

    suspend fun setSpendingLimitFromApi(newLimit: Double, context: Context) {
        val retrofitClient = RetrofitClient(context)
        val api = retrofitClient.userSpendingApi
        return withContext(Dispatchers.IO) {
            try {
                val response = api.setMonthlyLimit(UserSpendingRequest(newLimit))
                println("isValid: $response")
                if(response.monthlyLimit==newLimit) {
                    withContext(Dispatchers.IO) {
                        _uiState.update { currentState ->
                            currentState.copy(monthlyLimit = newLimit)
                        }
                    }
                }
            } catch (_: Exception) {
            }
        }
    }
    fun getSpendingLimit(): Double {
        return uiState.value.monthlyLimit
    }
    @RequiresApi(Build.VERSION_CODES.O)
    fun calculateExpenseByCategory() : HashMap<String, Double>{
        val expenseByCategory = HashMap<String, Double>()
        val categories = uiState.value.categories
        val expenses = uiState.value.expenses
        val currMonth = LocalDate.now().month
        val currYear = LocalDate.now().year
        categories.forEach { (_, name) ->
            expenseByCategory[name] = 0.0
        }
        expenses.forEach { expense ->
            val date = LocalDate.parse(expense.date, DateTimeFormatter.ISO_DATE_TIME)
            if(date.month==currMonth && date.year==currYear) {
                val category = categories[expense.category_id]
                if(expenseByCategory.containsKey(category)) {
                    if (category != null) {
                        expenseByCategory[category] = (expenseByCategory[category]?: 0.0) + expense.amount
                    }
                }
                else {
                    if (category != null) {
                        expenseByCategory[category] = expense.amount
                    }
                }
            }
        }
        _uiState.update { currentState->
            currentState.copy(
                expenseByCategory = expenseByCategory
            )

        }
        return expenseByCategory
    }

    fun getExpenseByCategory(): HashMap<String, Double> {
        return uiState.value.expenseByCategory
    }

    fun setListOfExpenseByCategory() {
        val expenseByCategory = uiState.value.expenseByCategory
        val listOfExpenseByCategory = ArrayList<CategoryExpense>()
        expenseByCategory.forEach { (category, expense) ->
            listOfExpenseByCategory.add(CategoryExpense(category, expense.toFloat()))
        }
        _uiState.update { currentState ->
            currentState.copy(
                listOfExpenseByCategory = listOfExpenseByCategory
            )
        }
    }

    fun getListOfExpenseByCategory() : List<CategoryExpense> {
        return uiState.value.listOfExpenseByCategory
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun calculateCurrentMonthExpense() {
        val expenses = uiState.value.expenses
        val currMonth = LocalDate.now().month
        val currYear = LocalDate.now().year
        var currentMonthExpense = 0.0
        expenses.forEach { expense ->
            val date = LocalDate.parse(expense.date, DateTimeFormatter.ISO_DATE_TIME)
            if(date.month==currMonth && date.year==currYear) {
                currentMonthExpense+=expense.amount
            }
        }
        _uiState.update { currentState ->
            currentState.copy(
                currentMonthExpense = currentMonthExpense
            )
        }
    }

    fun getCurrentMonthExpense(): Double {
        return uiState.value.currentMonthExpense
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun setExpensesByDate(date: String) {
        val selectedDate = LocalDate.parse(date)
        val expenses = uiState.value.expenses
        val expensesByDate = ArrayList<Expense>()
        expenses.forEach {expense->
            val expenseDate = LocalDate.parse(expense.date, DateTimeFormatter.ISO_DATE_TIME)
            if(expenseDate.dayOfMonth==selectedDate.dayOfMonth
                && expenseDate.month==selectedDate.month
                && expenseDate.year==selectedDate.year
                ) {
                expensesByDate.add(expense)
            }
        }
        _uiState.update { currentState ->
            currentState.copy(
                expensesByDate = expensesByDate
            )
        }
    }

    fun getExpensesByDate(): List<Expense> {
        return uiState.value.expensesByDate
    }
}
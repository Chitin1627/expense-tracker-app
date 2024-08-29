package com.example.expensetrackerapp.data.viewmodels

import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import com.example.expensetrackerapp.api.RetrofitClient
import com.example.expensetrackerapp.data.uistates.HomeScreenUiState
import com.example.expensetrackerapp.data.getUsername
import com.example.expensetrackerapp.data.saveToken
import com.example.expensetrackerapp.model.Category
import com.example.expensetrackerapp.model.CategoryExpense
import com.example.expensetrackerapp.model.Expense
import com.example.expensetrackerapp.model.ExpenseRequest
import com.example.expensetrackerapp.model.JwtToken
import com.example.expensetrackerapp.model.LoginRequest
import com.example.expensetrackerapp.model.RegisterRequest
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
import java.util.Date

class HomeScreenViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(HomeScreenUiState())
    val uiState: StateFlow<HomeScreenUiState> = _uiState.asStateFlow()

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


    fun getCategories(): HashMap<String, String> {
        return uiState.value.categories
    }

    fun getCategoriesNameMap(): HashMap<String, String> {
        return uiState.value.categoryNameMap
    }


    @RequiresApi(Build.VERSION_CODES.O)
    suspend fun getExpensesCurrentMonthFromApi(context: Context): List<Expense> {
        val retrofitClient = RetrofitClient(context)
        val api = retrofitClient.expenseApi
        return withContext(Dispatchers.IO) {
            try {
                val response = api.getUserExpensesCurrentMonth()
                setIsDataLoaded(true)
                response
            } catch(e: Exception) {
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
                emptyList()
            }
        }
    }


    suspend fun getSpendingLimitFromApi(context: Context) {
        val retrofitClient = RetrofitClient(context)
        val api = retrofitClient.userSpendingApi
        withContext(Dispatchers.IO) {
            try {
                val response = api.getUserSpending()
                if(response.username!=null) {
                    _uiState.update { currentState->
                        currentState.copy(
                            monthlyLimit = response.monthlyLimit
                        )
                    }
                }
            }
            catch (_: Exception) {

            }

        }
    }

    suspend fun setSpendingLimitFromApi(newLimit: Double, context: Context) {
        val retrofitClient = RetrofitClient(context)
        val api = retrofitClient.userSpendingApi
        return withContext(Dispatchers.IO) {
            try {
                val response = api.setMonthlyLimit(UserSpendingRequest(newLimit))
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
    fun calculateExpenseByCategory(expenses: List<Expense>) : HashMap<String, Double>{
        val expenseByCategory = HashMap<String, Double>()
        val categories = uiState.value.categories
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
    fun calculateCurrentMonthExpense(expenses: List<Expense>) {
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

//    @RequiresApi(Build.VERSION_CODES.O)
//    fun setExpensesByDate(date: String) {
//        val selectedDate = LocalDate.parse(date)
//        val expenses = uiState.value.expensesCurrentMonth
//        val expensesByDate = ArrayList<Expense>()
//        expenses.forEach {expense->
//            val expenseDate = LocalDate.parse(expense.date, DateTimeFormatter.ISO_DATE_TIME)
//            if(expenseDate.dayOfMonth==selectedDate.dayOfMonth
//                && expenseDate.month==selectedDate.month
//                && expenseDate.year==selectedDate.year
//                ) {
//                expensesByDate.add(expense)
//            }
//        }
//        _uiState.update { currentState ->
//            currentState.copy(
//                expensesByDate = expensesByDate
//            )
//        }
//    }

    suspend fun getExpensesByDateFromApi(context: Context, date: String): List<Expense> {
        val retrofitClient = RetrofitClient(context)
        val api = retrofitClient.expenseApi
        return withContext(Dispatchers.IO) {
            try {
                val response = api.getUserExpenseByDate(date)
                response
            } catch(e: Exception) {
                emptyList()
            }
        }
    }

    fun getExpensesByDate(): List<Expense> {
        return uiState.value.expensesByDate
    }

    fun setIsDataLoaded(isDataLoaded: Boolean) {
        _uiState.update { currentState ->
            currentState.copy(isDataLoaded = isDataLoaded)
        }
    }

    fun getIsDataLoaded(): Boolean {
        return uiState.value.isDataLoaded
    }

    fun setSelectedDate(date: String) {
        _uiState.update { currentState ->
            currentState.copy(
                selectedDate = date
            )
        }
    }

    fun getSelectedDate(): String {
        return uiState.value.selectedDate
    }

    fun clearState() {
        _uiState.update {currentState ->
            currentState.copy(
                monthlyLimit = 0.0,
                currentMonthExpense = 0.0,
                expensesByDate = emptyList(),
                categories = HashMap<String, String>(),
                categoryNameMap = HashMap<String, String>(),
                expenseByCategory = HashMap<String, Double>(),
                listOfExpenseByCategory = ArrayList<CategoryExpense>(),
                totalExpense = 0.0,
                isDataLoaded = false,
                selectedDate = ""
            )
        }
    }

}
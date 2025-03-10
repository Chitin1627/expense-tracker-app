package com.example.expensetrackerapp.data.viewmodels

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.expensetrackerapp.api.RetrofitClient
import com.example.expensetrackerapp.data.uistates.DebtScreenUiState
import com.example.expensetrackerapp.model.Debt
import com.example.expensetrackerapp.model.DebtRequest
import com.example.expensetrackerapp.model.Expense
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class DebtScreenViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(DebtScreenUiState())
    val uiState = _uiState.asStateFlow()


    suspend fun fetchDebts(context: Context) {
        val retrofitClient = RetrofitClient(context)
        val api = retrofitClient.debtApi
        withContext(Dispatchers.IO) {
            _uiState.value = _uiState.value.copy(isLoading = true)
            try {
                val response = api.getDebtsByUser()
                println(response)
                if (response.isSuccessful) {
                    _uiState.value = _uiState.value.copy(
                        debts = response.body() ?: ArrayList(),
                        isLoading = false
                    )
                } else {
                    _uiState.value = _uiState.value.copy(
                        errorMessage = "Error: ${response.code()}",
                        isLoading = false
                    )
                }
            } catch (e: Exception) {
                println(e.message)
                _uiState.value = _uiState.value.copy(
                    errorMessage = "Failed to load debts: ${e.message}",
                    isLoading = false
                )
            }
        }
    }


    suspend fun addDebt(debt: DebtRequest, context: Context): Debt {
        val retrofitClient = RetrofitClient(context)
        val api = retrofitClient.debtApi
        _uiState.value = _uiState.value.copy(isLoading = true)
        return try {
            val response = api.addDebt(debt)
            val newDebt: Debt? = response.body()
            if (response.isSuccessful) {
                fetchDebts(context)
            }
            if(newDebt!=null) newDebt
            else throw Exception("Error")
        } catch (e: Exception) {
            _uiState.value = _uiState.value.copy(
                errorMessage = "Failed to add debt: ${e.message}",
                isLoading = false
            )
            Debt()
        }
    }

    suspend fun deleteExpense(context: Context, id: String): Boolean {
        val retrofitClient = RetrofitClient(context)
        val api = retrofitClient.debtApi
        return withContext(Dispatchers.IO) {
            try {
                val response = api.deleteDebt(id)
                response.isSuccessful
            } catch(e: Exception) {
                false
            }
        }
    }

    suspend fun editDebt(context: Context): Boolean = withContext(Dispatchers.IO) {
        val editedDebt = getEditedDebt()
        try {
            val retrofitClient = RetrofitClient(context)
            val api = retrofitClient.debtApi
            val response = api.editDebt(editedDebt)
            println("EDIT: " + response.body())
            response.isSuccessful
        } catch (e: Exception) {
            println(e.message)
            false
        }
    }

    fun getDebts(): ArrayList<Debt> {
        return uiState.value.debts
    }

    fun getEditedDebt(): Debt {
        return uiState.value.editedDebt
    }

    fun setEditedDebt(editedDebt: Debt) {
        _uiState.update { currentState ->
            currentState.copy(
                editedDebt = editedDebt
            )
        }
    }
}

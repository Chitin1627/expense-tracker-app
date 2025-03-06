package com.example.expensetrackerapp.data.viewmodels

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.expensetrackerapp.api.RetrofitClient
import com.example.expensetrackerapp.data.uistates.DebtScreenUiState
import com.example.expensetrackerapp.model.Debt
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
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
                        debts = response.body() ?: emptyList(),
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


    fun addDebt(debt: Debt, context: Context) {
        val retrofitClient = RetrofitClient(context)
        val api = retrofitClient.debtApi
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            try {
                val response = api.addDebt(debt)
                if (response.isSuccessful) {
                    fetchDebts(context)
                }
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    errorMessage = "Failed to add debt: ${e.message}",
                    isLoading = false
                )
            }
        }
    }

    fun getDebts(): List<Debt> {
        return uiState.value.debts
    }
}

package com.example.expensetrackerapp.data.viewmodels

import android.content.Context
import androidx.lifecycle.ViewModel
import com.example.expensetrackerapp.api.RetrofitClient
import com.example.expensetrackerapp.data.saveToken
import com.example.expensetrackerapp.data.uistates.AuthenticationUiState
import com.example.expensetrackerapp.model.PasswordChangeResponse
import com.example.expensetrackerapp.model.JwtToken
import com.example.expensetrackerapp.model.LoginRequest
import com.example.expensetrackerapp.model.PasswordChangeRequest
import com.example.expensetrackerapp.model.RegisterRequest
import com.example.expensetrackerapp.model.UserDetails
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class AuthenticationViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(AuthenticationUiState())
    val uiState: StateFlow<AuthenticationUiState> = _uiState.asStateFlow()

    private fun getToken(): String {
        return uiState.value.token
    }
    fun setUsernamePassword(username: String, password: String) {
        _uiState.update {currentState ->
            currentState.copy(
                username = username,
                password = password
            )
        }
    }


    fun setEmail(email: String) {
        _uiState.update { currentState ->
            currentState.copy(
                email = email
            )
        }
    }

    fun getEmail(): String {
        return uiState.value.email
    }

    fun getUsername(): String {
        return uiState.value.username
    }

    fun getPassword(): String {
        return uiState.value.password
    }

    fun setToken(token: String) {
        _uiState.update { currentState ->
            currentState.copy(
                token = token
            )
        }
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
                saveToken(context, token.token)
                withContext(Dispatchers.Main) {
                    onSuccess(token.token)
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    onError(e.message ?: "An error occurred")
                }
            }
        }
    }

    suspend fun performRegister(context: Context, onSuccess: (String) -> Unit, onError: (String) -> Unit) {
        val retrofitClient = RetrofitClient(context)
        val api = retrofitClient.authApi
        val username = getUsername()
        val password = getPassword()
        val email = getEmail()
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val token = api.register(RegisterRequest(username,email, password))
                saveToken(context, token.token)
                withContext(Dispatchers.Main) {
                    onSuccess(token.token)
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
        val token = getToken()
        return withContext(Dispatchers.IO) {
            try {
                val response = api.validateToken(JwtToken(token))
                val isValid = response == "true"
                withContext(Dispatchers.Main) {
                    _uiState.update { currentState ->
                        currentState.copy(isTokenValid = isValid)
                    }
                }
                isValid
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    _uiState.update { currentState ->
                        currentState.copy(isTokenValid = false)
                    }
                }
                false
            }
        }
    }

    suspend fun getUserDetailsFromApi(context: Context): UserDetails {
        val retrofitClient = RetrofitClient(context)
        val api = retrofitClient.userApi
        return withContext(Dispatchers.IO) {
            try {
                val response = api.getUserDetails()
                setIsDataLoaded(true)
                _uiState.update { currentState ->
                    currentState.copy(
                        username = response.username,
                        email = response.email
                    )
                }
                response
            } catch (e: Exception) {
                UserDetails()
            }
        }
    }


    suspend fun changePassword(context: Context, passwordChangeRequest: PasswordChangeRequest): PasswordChangeResponse {
        val retrofitClient = RetrofitClient(context)
        val api = retrofitClient.userApi
        return withContext(Dispatchers.IO) {
            try {
                val response = api.changePassword(passwordChangeRequest)
                println(response)
                if(response.isSuccessful) {
                    PasswordChangeResponse(
                        true,
                        "Password Changed Successfully"
                    )
                } else {
                    println("Error: ${response.errorBody()?.string()}")
                    PasswordChangeResponse(
                        false,
                        "Current Password is wrong"
                    )
                }
            } catch (e: Exception) {
                println(e.message)
                PasswordChangeResponse(
                    false,
                    "Server Error"
                )
            }
        }
    }

    suspend fun deleteAccount(context: Context): Boolean {
        val retrofitClient = RetrofitClient(context)
        val api = retrofitClient.userApi
        return withContext(Dispatchers.IO) {
            try {
                val response = api.deleteUser()
                response.isSuccessful
            } catch (e: Exception) {
                false
            }
        }
    }

    fun getIsDataLoaded(): Boolean {
        return uiState.value.isDataLoaded
    }
    private fun setIsDataLoaded(isDataLoaded: Boolean) {
        _uiState.update { currentState ->
            currentState.copy(
                isDataLoaded = isDataLoaded
            )
        }
    }

}
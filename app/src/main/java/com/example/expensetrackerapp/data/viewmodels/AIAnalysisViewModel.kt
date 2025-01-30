package com.example.expensetrackerapp.data.viewmodels

import android.content.Context
import androidx.lifecycle.ViewModel
import com.example.expensetrackerapp.api.RetrofitClient
import com.example.expensetrackerapp.data.uistates.AIAnalysisUiState
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.withContext

data class AIResponse(
    val candidates: List<Candidate>
)

data class Candidate(
    val content: Content
)

data class Content(
    val parts: List<Part>
)

data class Part(
    val text: String
)
class AIAnalysisViewModel: ViewModel() {
    private val _uiState = MutableStateFlow(AIAnalysisUiState())
    val uiState: StateFlow<AIAnalysisUiState> = _uiState.asStateFlow()

    suspend fun askAi(context: Context): Boolean = withContext(Dispatchers.IO){
        try {
            val retrofitClient = RetrofitClient(context)
            val api = retrofitClient.aiAnalyseApi
            val response = api.getAIAnalysis()
            println(response.body())
            if(response==null) false
            response.body()?.let { extractText(it.string()) }?.let { setResponse(it) }
            response.isSuccessful
        }
        catch(e: Exception) {
            println(e.message)
            false
        }
    }

    fun extractText(json: String): String {
        return try {
            val aiResponse = Gson().fromJson(json, AIResponse::class.java)
            aiResponse.candidates.firstOrNull()?.content?.parts?.firstOrNull()?.text ?: "No analysis available"
        } catch (e: Exception) {
            "Error parsing AI response"
        }
    }

    fun setResponse(response: String) {
        _uiState.update { currentState ->
            currentState.copy(
                response = response
            )
        }
    }

    fun getResponse(): String {
        return uiState.value.response
    }
}
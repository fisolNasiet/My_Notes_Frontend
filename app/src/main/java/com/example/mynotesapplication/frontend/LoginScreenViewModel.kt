package com.example.mynotesapplication.frontend

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.mynotesapplication.NoteApplication
import com.example.mynotesapplication.backend.data.AppRepository
import com.example.mynotesapplication.backend.network.TokenResponse
import com.example.mynotesapplication.backend.security.TokenManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import retrofit2.Response

sealed interface LoginUiState {
    data object Idle : LoginUiState
    data object Loading : LoginUiState
    data object Success : LoginUiState
    data class Error(val message: String) : LoginUiState
}

@Serializable
private data class LoginServerErrorResponse(val errors: List<String> = emptyList())

private val loginErrorJson = Json { ignoreUnknownKeys = true }

private fun parseLoginErrorMessage(response: Response<TokenResponse>): String {
    val body = response.errorBody()?.string()
    if (body.isNullOrBlank()) {
        return "Request failed (${response.code()})"
    }
    return try {
        val parsed = loginErrorJson.decodeFromString<LoginServerErrorResponse>(body)
        parsed.errors.takeIf { it.isNotEmpty() }?.joinToString(", ")
            ?: "Request failed (${response.code()})"
    } catch (e: Exception) {
        "Request failed (${response.code()})"
    }
}

class LoginScreenViewModel(
    private val appRepository: AppRepository,
    private val tokenManager: TokenManager
): ViewModel(){

    private val _uiState = MutableStateFlow<LoginUiState>(LoginUiState.Idle)
    val uiState: StateFlow<LoginUiState> = _uiState.asStateFlow()

    fun login(email: String, password: String) {
        if (email.isBlank() || !email.contains("@")) {
            _uiState.value = LoginUiState.Error("Enter a valid email address")
            return
        }
        if (password.isBlank()) {
            _uiState.value = LoginUiState.Error("Password can't be empty")
            return
        }

        viewModelScope.launch {
            _uiState.value = LoginUiState.Loading
            Log.d("CHECK_LOGIN", "--- Login Button Pressed ---")

            try {
                val response = appRepository.login(email, password)

                if (response.isSuccessful) {
                    val tokens = response.body()
                    if (tokens != null) {
                        Log.d("CHECK_LOGIN", "SUCCESS")
                        tokenManager.saveTokens(tokens.accessToken, tokens.refreshToken)
                        _uiState.value = LoginUiState.Success
                    } else {
                        Log.e("CHECK_LOGIN", "SUCCESS response but empty body")
                        _uiState.value = LoginUiState.Error("Login succeeded but no tokens were returned")
                    }
                } else {
                    Log.e("CHECK_LOGIN", "SERVER REJECTED: ${response.code()}")
                    _uiState.value = LoginUiState.Error(parseLoginErrorMessage(response))
                }
            } catch (e: Exception) {
                Log.e("CHECK_LOGIN", "CRASH/EXCEPTION: ${e.message}")
                _uiState.value = LoginUiState.Error(e.message ?: "Unable to reach the server")
            }
        }
    }

    fun resetState() {
        _uiState.value = LoginUiState.Idle
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[ ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY ] as NoteApplication)
                val appRepository = application.container.appRepository
                val tokenManager = application.container.tokenManager
                LoginScreenViewModel(appRepository = appRepository, tokenManager = tokenManager)
            }
        }
    }
}

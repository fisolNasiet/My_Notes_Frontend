package com.example.mynotesapplication.frontend

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.mynotesapplication.NoteApplication
import com.example.mynotesapplication.backend.data.AppRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import retrofit2.Response

sealed interface RegisterUiState {
    data object Idle : RegisterUiState
    data object Loading : RegisterUiState
    data object Success : RegisterUiState
    data class Error(val message: String) : RegisterUiState
}

@Serializable
private data class ServerErrorResponse(val errors: List<String> = emptyList())

private val PASSWORD_POLICY_REGEX = Regex("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d).{9,}$")
private val errorJson = Json { ignoreUnknownKeys = true }

private fun parseErrorMessage(response: Response<Void>): String {
    val body = response.errorBody()?.string()
    if (body.isNullOrBlank()) {
        return "Request failed (${response.code()})"
    }
    return try {
        val parsed = errorJson.decodeFromString<ServerErrorResponse>(body)
        parsed.errors.takeIf { it.isNotEmpty() }?.joinToString(", ")
            ?: "Request failed (${response.code()})"
    } catch (e: Exception) {
        "Request failed (${response.code()})"
    }
}

class RegisterScreenViewModel(
    private val appRepository: AppRepository
): ViewModel(){

    private val _uiState = MutableStateFlow<RegisterUiState>(RegisterUiState.Idle)
    val uiState: StateFlow<RegisterUiState> = _uiState.asStateFlow()

    fun register(email: String, password: String, confirmPassword: String) {
        if (email.isBlank() || !email.contains("@")) {
            _uiState.value = RegisterUiState.Error("Enter a valid email address")
            return
        }
        if (password.isBlank()) {
            _uiState.value = RegisterUiState.Error("Password can't be empty")
            return
        }
        if (password != confirmPassword) {
            _uiState.value = RegisterUiState.Error("Passwords don't match")
            return
        }
        if (!PASSWORD_POLICY_REGEX.matches(password)) {
            _uiState.value = RegisterUiState.Error(
                "Password must be at least 9 characters and include an uppercase letter, a lowercase letter, and a digit"
            )
            return
        }

        viewModelScope.launch {
            _uiState.value = RegisterUiState.Loading
            Log.d("CHECK_REGISTER", "--- Register Button Pressed ---")

            try {
                val response = appRepository.register(email, password)

                if (response.isSuccessful) {
                    Log.d("CHECK_REGISTER", "SUCCESS: ${response.body()}")
                    _uiState.value = RegisterUiState.Success
                } else {
                    Log.e("CHECK_REGISTER", "SERVER REJECTED: ${response.code()}")
                    _uiState.value = RegisterUiState.Error(parseErrorMessage(response))
                }
            } catch (e: Exception) {
                Log.e("CHECK_REGISTER", "CRASH/EXCEPTION: ${e.message}")
                _uiState.value = RegisterUiState.Error(e.message ?: "Unable to reach the server")
            }
        }
    }

    fun resetState() {
        _uiState.value = RegisterUiState.Idle
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[ ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY ] as NoteApplication)
                val appContainer = application.container.appRepository
                RegisterScreenViewModel(appRepository = appContainer)
            }
        }
    }
}

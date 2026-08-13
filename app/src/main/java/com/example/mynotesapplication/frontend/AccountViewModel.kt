package com.example.mynotesapplication.frontend

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.mynotesapplication.NoteApplication
import com.example.mynotesapplication.backend.data.AppRepository
import com.example.mynotesapplication.backend.security.TokenManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

sealed interface DeleteAccountUiState {
    data object Idle : DeleteAccountUiState
    data object Deleting : DeleteAccountUiState
    data object Success : DeleteAccountUiState
    data class Error(val message: String) : DeleteAccountUiState
}

class AccountViewModel(
    private val appRepository: AppRepository,
    private val tokenManager: TokenManager
) : ViewModel() {

    private val _uiState = MutableStateFlow<DeleteAccountUiState>(DeleteAccountUiState.Idle)
    val uiState: StateFlow<DeleteAccountUiState> = _uiState.asStateFlow()

    fun deleteAccount() {
        viewModelScope.launch {
            _uiState.value = DeleteAccountUiState.Deleting
            try {
                val response = appRepository.deleteAccount()
                if (response.isSuccessful) {
                    tokenManager.clearTokens()
                    _uiState.value = DeleteAccountUiState.Success
                } else {
                    _uiState.value = DeleteAccountUiState.Error("Failed to delete account: ${response.code()}")
                }
            } catch (e: Exception) {
                _uiState.value = DeleteAccountUiState.Error(e.message ?: "Unable to delete account")
            }
        }
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as NoteApplication)
                val appRepository = application.container.appRepository
                val tokenManager = application.container.tokenManager
                AccountViewModel(appRepository = appRepository, tokenManager = tokenManager)
            }
        }
    }
}

package com.example.mynotesapplication.frontend

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.mynotesapplication.NoteApplication
import com.example.mynotesapplication.backend.data.AppRepository
import com.example.mynotesapplication.backend.model.Note
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

sealed interface NotesUiState {
    data object Loading : NotesUiState
    data class Success(val notes: List<Note>) : NotesUiState
    data class Error(val message: String) : NotesUiState
}

sealed interface CreateNoteUiState {
    data object Idle : CreateNoteUiState
    data object Saving : CreateNoteUiState
    data object Success : CreateNoteUiState
    data class Error(val message: String) : CreateNoteUiState
}

sealed interface DeleteNoteUiState {
    data object Idle : DeleteNoteUiState
    data object Deleting : DeleteNoteUiState
    data object Success : DeleteNoteUiState
    data class Error(val message: String) : DeleteNoteUiState
}

class NotesViewModel(
    private val appRepository: AppRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<NotesUiState>(NotesUiState.Loading)
    val uiState: StateFlow<NotesUiState> = _uiState.asStateFlow()

    private val _createState = MutableStateFlow<CreateNoteUiState>(CreateNoteUiState.Idle)
    val createState: StateFlow<CreateNoteUiState> = _createState.asStateFlow()

    private val _deleteState = MutableStateFlow<DeleteNoteUiState>(DeleteNoteUiState.Idle)
    val deleteState: StateFlow<DeleteNoteUiState> = _deleteState.asStateFlow()

    init {
        loadNotes()
    }

    fun loadNotes() {
        viewModelScope.launch {
            _uiState.value = NotesUiState.Loading
            try {
                val response = appRepository.getNotes()
                _uiState.value = if (response.isSuccessful) {
                    NotesUiState.Success(response.body().orEmpty())
                } else {
                    NotesUiState.Error("Failed to load notes: ${response.code()}")
                }
            } catch (e: Exception) {
                _uiState.value = NotesUiState.Error(e.message ?: "Unable to load notes")
            }
        }
    }

    fun createNote(title: String, content: String, color: Long) {
        viewModelScope.launch {
            _createState.value = CreateNoteUiState.Saving
            try {
                val response = appRepository.createNote(Note(title = title, content = content, color = color))
                if (response.isSuccessful) {
                    _createState.value = CreateNoteUiState.Success
                    loadNotes()
                } else {
                    _createState.value = CreateNoteUiState.Error("Failed to save note: ${response.code()}")
                }
            } catch (e: Exception) {
                _createState.value = CreateNoteUiState.Error(e.message ?: "Unable to save note")
            }
        }
    }

    fun resetCreateState() {
        _createState.value = CreateNoteUiState.Idle
    }

    fun deleteNote(id: Long) {
        viewModelScope.launch {
            _deleteState.value = DeleteNoteUiState.Deleting
            try {
                val response = appRepository.deleteNote(id)
                if (response.isSuccessful) {
                    _deleteState.value = DeleteNoteUiState.Success
                    loadNotes()
                } else {
                    _deleteState.value = DeleteNoteUiState.Error("Failed to delete note: ${response.code()}")
                }
            } catch (e: Exception) {
                _deleteState.value = DeleteNoteUiState.Error(e.message ?: "Unable to delete note")
            }
        }
    }

    fun resetDeleteState() {
        _deleteState.value = DeleteNoteUiState.Idle
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as NoteApplication)
                val appRepository = application.container.appRepository
                NotesViewModel(appRepository = appRepository)
            }
        }
    }
}

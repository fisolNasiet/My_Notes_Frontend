package com.example.mynotesapplication.frontend

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.GridView
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.material3.TextButton
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.mynotesapplication.backend.model.Note
import com.example.mynotesapplication.ui.components.CircleIconButton
import com.example.mynotesapplication.ui.components.FilterPill
import com.example.mynotesapplication.ui.components.NoteCard
import com.example.mynotesapplication.ui.components.PillButton
import com.example.mynotesapplication.ui.components.dottedGridBackground
import com.example.mynotesapplication.ui.theme.ElevatedBlack
import com.example.mynotesapplication.ui.theme.ErrorRed
import com.example.mynotesapplication.ui.theme.FrostedGlassContainer
import com.example.mynotesapplication.ui.theme.OnBlack
import com.example.mynotesapplication.ui.theme.OnErrorRed
import com.example.mynotesapplication.ui.theme.PitchBlack

@Composable
fun NotesListScreen(
    onAddNoteClick: () -> Unit,
    onAccountClick: () -> Unit,
    notesViewModel: NotesViewModel
) {
    val uiState by notesViewModel.uiState.collectAsState()
    val deleteState by notesViewModel.deleteState.collectAsState()
    var noteToDelete by remember { mutableStateOf<Note?>(null) }
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(deleteState) {
        val state = deleteState
        if (state is DeleteNoteUiState.Error) {
            snackbarHostState.showSnackbar(state.message)
            notesViewModel.resetDeleteState()
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Scaffold(
            containerColor = PitchBlack,
            snackbarHost = { SnackbarHost(snackbarHostState) },
        ) { innerPadding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .dottedGridBackground(),
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp, vertical = 16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Text(
                        "My Notes",
                        style = MaterialTheme.typography.displayLarge,
                        color = OnBlack,
                    )
                    CircleIconButton(
                        icon = Icons.Rounded.GridView,
                        contentDescription = "Account",
                        onClick = onAccountClick,
                        containerColor = ElevatedBlack,
                        tint = OnBlack,
                    )
                }

                Box(modifier = Modifier.fillMaxSize()) {
                    when (val state = uiState) {
                        is NotesUiState.Loading -> CircularProgressIndicator(
                            modifier = Modifier.align(Alignment.Center),
                            color = OnBlack,
                        )

                        is NotesUiState.Error -> Column(
                            modifier = Modifier.align(Alignment.Center),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            Text(state.message, color = ErrorRed)
                        }

                        is NotesUiState.Success -> {
                            Column(modifier = Modifier.fillMaxSize()) {
                                FilterPill(
                                    label = "All",
                                    count = state.notes.size,
                                    modifier = Modifier.padding(start = 20.dp, bottom = 12.dp),
                                )
                                if (state.notes.isEmpty()) {
                                    Text(
                                        "No notes yet",
                                        style = MaterialTheme.typography.bodyMedium,
                                        color = OnBlack,
                                        modifier = Modifier
                                            .align(Alignment.CenterHorizontally)
                                            .padding(top = 32.dp),
                                    )
                                } else {
                                    LazyVerticalStaggeredGrid(
                                        columns = StaggeredGridCells.Fixed(2),
                                        verticalItemSpacing = 12.dp,
                                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                                        contentPadding = PaddingValues(
                                            start = 16.dp,
                                            end = 16.dp,
                                            bottom = 120.dp,
                                        ),
                                        modifier = Modifier.fillMaxSize(),
                                    ) {
                                        items(state.notes, key = { it.id ?: it.hashCode().toString() }) { note ->
                                            NoteCard(note, onDeleteClick = { noteToDelete = note })
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        FrostedGlassContainer(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 24.dp)
                .size(width = 88.dp, height = 64.dp),
        ) {
            CircleIconButton(
                icon = Icons.Rounded.Add,
                contentDescription = "Add note",
                onClick = onAddNoteClick,
                containerColor = PitchBlack,
                tint = OnBlack,
                size = 56.dp,
                modifier = Modifier.align(Alignment.Center),
            )
        }
    }

    noteToDelete?.let { note ->
        AlertDialog(
            onDismissRequest = { noteToDelete = null },
            containerColor = ElevatedBlack,
            titleContentColor = OnBlack,
            textContentColor = OnBlack,
            title = { Text("Delete note?") },
            text = { Text("This cannot be undone.") },
            confirmButton = {
                PillButton(
                    text = "Delete",
                    containerColor = ErrorRed,
                    contentColor = OnErrorRed,
                    onClick = {
                        note.id?.let { notesViewModel.deleteNote(it) }
                        noteToDelete = null
                    }
                )
            },
            dismissButton = {
                TextButton(onClick = { noteToDelete = null }) {
                    Text("Cancel", color = OnBlack)
                }
            }
        )
    }
}

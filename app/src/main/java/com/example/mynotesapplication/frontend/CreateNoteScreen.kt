package com.example.mynotesapplication.frontend

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.mynotesapplication.ui.theme.NoteAccentColors

@Composable
fun CreateNoteScreen(
    onNoteSaved: () -> Unit,
    notesViewModel: NotesViewModel
) {
    val createState by notesViewModel.createState.collectAsState()
    var title by remember { mutableStateOf("") }
    var content by remember { mutableStateOf("") }
    var selectedColor by remember { mutableStateOf(NoteAccentColors.first()) }

    LaunchedEffect(createState) {
        if (createState is CreateNoteUiState.Success) {
            onNoteSaved()
            notesViewModel.resetCreateState()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text("New note", style = MaterialTheme.typography.titleLarge)

        OutlinedTextField(
            value = title,
            onValueChange = { title = it },
            label = { Text("Title") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = content,
            onValueChange = { content = it },
            label = { Text("Content") },
            modifier = Modifier
                .fillMaxWidth()
                .height(160.dp)
        )

        Text("Color", style = MaterialTheme.typography.labelLarge)
        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            NoteAccentColors.forEach { color ->
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .clip(CircleShape)
                        .background(color)
                        .border(
                            width = if (color == selectedColor) 3.dp else 0.dp,
                            color = MaterialTheme.colorScheme.primary,
                            shape = CircleShape
                        )
                        .clickable { selectedColor = color }
                )
            }
        }

        val createErrorMessage = (createState as? CreateNoteUiState.Error)?.message
        if (createErrorMessage != null) {
            Text(createErrorMessage, color = MaterialTheme.colorScheme.error)
        }

        Button(
            onClick = {
                notesViewModel.createNote(
                    title = title,
                    content = content,
                    color = selectedColor.value.toLong()
                )
            },
            enabled = createState !is CreateNoteUiState.Saving && title.isNotBlank(),
            modifier = Modifier.fillMaxWidth()
        ) {
            if (createState is CreateNoteUiState.Saving) {
                CircularProgressIndicator(modifier = Modifier.padding(2.dp))
            } else {
                Text("Save note")
            }
        }
    }
}

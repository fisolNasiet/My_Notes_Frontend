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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.mynotesapplication.ui.components.CircleIconButton
import com.example.mynotesapplication.ui.components.PillButton
import com.example.mynotesapplication.ui.theme.NoteAccentColors
import com.example.mynotesapplication.ui.theme.OnPaleYellow
import com.example.mynotesapplication.ui.theme.OnPaleYellowMuted
import com.example.mynotesapplication.ui.theme.PaleYellow
import com.example.mynotesapplication.ui.theme.PaleYellowSurface
import com.example.mynotesapplication.ui.theme.PitchBlack

@Composable
fun CreateNoteScreen(
    onNoteSaved: () -> Unit,
    onBackClick: () -> Unit,
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
            .background(PaleYellow)
            .padding(horizontal = 20.dp, vertical = 16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        CircleIconButton(
            icon = Icons.AutoMirrored.Rounded.ArrowBack,
            contentDescription = "Back",
            onClick = onBackClick,
            containerColor = Color.Black.copy(alpha = 0.12f),
            tint = OnPaleYellow,
        )

        TextField(
            value = title,
            onValueChange = { title = it },
            textStyle = MaterialTheme.typography.displayLarge.copy(color = OnPaleYellow),
            placeholder = {
                Text("Untitled", style = MaterialTheme.typography.displayLarge, color = OnPaleYellowMuted)
            },
            colors = TextFieldDefaults.colors(
                focusedContainerColor = Color.Transparent,
                unfocusedContainerColor = Color.Transparent,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                cursorColor = OnPaleYellow,
            ),
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )

        TextField(
            value = content,
            onValueChange = { content = it },
            textStyle = MaterialTheme.typography.bodyLarge.copy(color = OnPaleYellow),
            placeholder = {
                Text("Start writing…", style = MaterialTheme.typography.bodyLarge, color = OnPaleYellowMuted)
            },
            colors = TextFieldDefaults.colors(
                focusedContainerColor = Color.Transparent,
                unfocusedContainerColor = Color.Transparent,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                cursorColor = OnPaleYellow,
            ),
            modifier = Modifier
                .fillMaxWidth()
                .height(220.dp)
        )

        Text(
            "Color",
            style = MaterialTheme.typography.labelLarge,
            color = OnPaleYellowMuted,
        )
        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            NoteAccentColors.forEach { color ->
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                        .background(color)
                        .border(
                            width = if (color == selectedColor) 3.dp else 0.dp,
                            color = OnPaleYellow,
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

        PillButton(
            text = "Save note",
            onClick = {
                notesViewModel.createNote(
                    title = title,
                    content = content,
                    color = selectedColor.value.toLong()
                )
            },
            enabled = createState !is CreateNoteUiState.Saving && title.isNotBlank(),
            loading = createState is CreateNoteUiState.Saving,
            containerColor = PitchBlack,
            contentColor = PaleYellowSurface,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp)
        )
    }
}

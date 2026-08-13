package com.example.mynotesapplication.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.mynotesapplication.backend.model.Note
import com.example.mynotesapplication.ui.theme.AccentCoral
import com.example.mynotesapplication.ui.theme.MyNotesApplicationTheme
import com.example.mynotesapplication.ui.theme.OnAccent

/**
 * A single masonry-grid note card: flat (no shadow), tinted with the note's
 * accent color, sized by its content for a genuine staggered look.
 */
@Composable
fun NoteCard(note: Note, onDeleteClick: () -> Unit, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color(note.color)),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = androidx.compose.foundation.layout.Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top,
            ) {
                Text(
                    note.title,
                    style = MaterialTheme.typography.titleMedium,
                    color = OnAccent,
                    modifier = Modifier.weight(1f),
                )
                IconButton(onClick = onDeleteClick, modifier = Modifier.size(32.dp)) {
                    Icon(
                        Icons.Rounded.Delete,
                        contentDescription = "Delete note",
                        tint = OnAccent,
                        modifier = Modifier.size(18.dp),
                    )
                }
            }
            Text(
                note.content,
                style = MaterialTheme.typography.bodyMedium,
                color = OnAccent,
                maxLines = 6,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.padding(top = 4.dp),
            )
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF0A0A0A)
@Composable
private fun NoteCardPreview() {
    MyNotesApplicationTheme {
        NoteCard(
            note = Note(
                id = 1,
                title = "Plan for The Day",
                content = "Buy food, GYM, Invest. A short preview of note content to check wrapping and corner radius.",
                color = AccentCoral.value.toLong(),
            ),
            onDeleteClick = {},
            modifier = Modifier.padding(16.dp),
        )
    }
}

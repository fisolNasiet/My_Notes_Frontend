package com.example.mynotesapplication.frontend

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.mynotesapplication.ui.components.CircleIconButton
import com.example.mynotesapplication.ui.components.PillButton
import com.example.mynotesapplication.ui.theme.ElevatedBlack
import com.example.mynotesapplication.ui.theme.ErrorRed
import com.example.mynotesapplication.ui.theme.OnBlack
import com.example.mynotesapplication.ui.theme.OnErrorRed
import com.example.mynotesapplication.ui.theme.PitchBlack

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AccountScreen(
    onBackClick: () -> Unit,
    onAccountDeleted: () -> Unit,
    accountViewModel: AccountViewModel
) {
    val uiState by accountViewModel.uiState.collectAsState()
    var showConfirmDialog by remember { mutableStateOf(false) }

    LaunchedEffect(uiState) {
        if (uiState is DeleteAccountUiState.Success) {
            onAccountDeleted()
        }
    }

    Scaffold(
        containerColor = PitchBlack,
        topBar = {
            TopAppBar(
                title = { Text("Account", style = MaterialTheme.typography.titleLarge, color = OnBlack) },
                navigationIcon = {
                    CircleIconButton(
                        icon = Icons.AutoMirrored.Rounded.ArrowBack,
                        contentDescription = "Back",
                        onClick = onBackClick,
                        containerColor = ElevatedBlack,
                        tint = OnBlack,
                        modifier = Modifier.padding(start = 8.dp),
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = PitchBlack),
            )
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                when (val state = uiState) {
                    is DeleteAccountUiState.Deleting -> CircularProgressIndicator(color = ErrorRed)
                    is DeleteAccountUiState.Error -> Text(
                        state.message,
                        color = ErrorRed
                    )
                    else -> {}
                }

                PillButton(
                    text = "Delete Account",
                    onClick = { showConfirmDialog = true },
                    containerColor = ErrorRed,
                    contentColor = OnErrorRed,
                    enabled = uiState !is DeleteAccountUiState.Deleting,
                )
            }
        }
    }

    if (showConfirmDialog) {
        AlertDialog(
            onDismissRequest = { showConfirmDialog = false },
            containerColor = ElevatedBlack,
            titleContentColor = OnBlack,
            textContentColor = OnBlack,
            title = { Text("Delete account?") },
            text = { Text("This will permanently delete your account and all notes.") },
            confirmButton = {
                PillButton(
                    text = "Delete",
                    containerColor = ErrorRed,
                    contentColor = OnErrorRed,
                    onClick = {
                        showConfirmDialog = false
                        accountViewModel.deleteAccount()
                    }
                )
            },
            dismissButton = {
                TextButton(onClick = { showConfirmDialog = false }) {
                    Text("Cancel", color = OnBlack)
                }
            }
        )
    }
}

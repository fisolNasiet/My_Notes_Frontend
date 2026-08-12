package com.example.mynotesapplication.frontend

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.text.KeyboardOptions
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.mynotesapplication.ui.theme.MyNotesApplicationTheme

@Composable
fun RegisterScreen(
    onRegisterSuccess: () -> Unit,
    onBackToLoginClick: () -> Unit,
    registerScreenViewModel: RegisterScreenViewModel = viewModel(factory = RegisterScreenViewModel.Factory)
) {
    val uiState by registerScreenViewModel.uiState.collectAsState()
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }

    LaunchedEffect(uiState) {
        if (uiState is RegisterUiState.Success) {
            onRegisterSuccess()
            registerScreenViewModel.resetState()
        }
    }

    RegisterScreenContent(
        email = email,
        password = password,
        confirmPassword = confirmPassword,
        onEmailChange = { email = it },
        onPasswordChange = { password = it },
        onConfirmPasswordChange = { confirmPassword = it },
        uiState = uiState,
        onRegisterClick = { registerScreenViewModel.register(email, password, confirmPassword) },
        onBackToLoginClick = onBackToLoginClick
    )
}

@Composable
fun RegisterScreenContent(
    email: String,
    password: String,
    confirmPassword: String,
    onEmailChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onConfirmPasswordChange: (String) -> Unit,
    uiState: RegisterUiState,
    onRegisterClick: () -> Unit,
    onBackToLoginClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(text = "Create account", style = MaterialTheme.typography.titleLarge)

        OutlinedTextField(
            value = email,
            onValueChange = onEmailChange,
            label = { Text("Email") },
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = password,
            onValueChange = onPasswordChange,
            label = { Text("Password") },
            singleLine = true,
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            supportingText = { Text("At least 9 characters, with upper/lowercase letters and a digit") },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = confirmPassword,
            onValueChange = onConfirmPasswordChange,
            label = { Text("Confirm password") },
            singleLine = true,
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            modifier = Modifier.fillMaxWidth()
        )

        if (uiState is RegisterUiState.Error) {
            Text(text = uiState.message, color = MaterialTheme.colorScheme.error)
        }

        Button(
            onClick = onRegisterClick,
            enabled = uiState !is RegisterUiState.Loading,
            modifier = Modifier.fillMaxWidth()
        ) {
            if (uiState is RegisterUiState.Loading) {
                CircularProgressIndicator(modifier = Modifier.padding(2.dp))
            } else {
                Text("Register")
            }
        }

        TextButton(
            onClick = onBackToLoginClick,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Already have an account? Login")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun RegisterScreenPreview() {
    MyNotesApplicationTheme {
        RegisterScreenContent(
            email = "",
            password = "",
            confirmPassword = "",
            onEmailChange = {},
            onPasswordChange = {},
            onConfirmPasswordChange = {},
            uiState = RegisterUiState.Idle,
            onRegisterClick = {},
            onBackToLoginClick = {}
        )
    }
}

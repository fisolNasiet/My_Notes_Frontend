package com.example.mynotesapplication.frontend

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
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
import com.example.mynotesapplication.ui.components.PillButton
import com.example.mynotesapplication.ui.theme.AccentYellow
import com.example.mynotesapplication.ui.theme.MyNotesApplicationTheme
import com.example.mynotesapplication.ui.theme.OnAccent
import com.example.mynotesapplication.ui.theme.OnBlack
import com.example.mynotesapplication.ui.theme.OnBlackMuted
import com.example.mynotesapplication.ui.theme.PitchBlack

@Composable
fun LoginScreen(
    onLoginSuccess: () -> Unit,
    onRegisterClick: () -> Unit,
    loginScreenViewModel: LoginScreenViewModel = viewModel(factory = LoginScreenViewModel.Factory)
) {
    val uiState by loginScreenViewModel.uiState.collectAsState()
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    LaunchedEffect(uiState) {
        if (uiState is LoginUiState.Success) {
            onLoginSuccess()
            loginScreenViewModel.resetState()
        }
    }

    LoginScreenContent(
        email = email,
        password = password,
        onEmailChange = { email = it },
        onPasswordChange = { password = it },
        uiState = uiState,
        onLoginClick = { loginScreenViewModel.login(email, password) },
        onRegisterClick = onRegisterClick
    )
}

@Composable
fun LoginScreenContent(
    email: String,
    password: String,
    onEmailChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    uiState: LoginUiState,
    onLoginClick: () -> Unit,
    onRegisterClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(PitchBlack)
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(text = "Welcome back", style = MaterialTheme.typography.headlineMedium, color = OnBlack)

        OutlinedTextField(
            value = email,
            onValueChange = onEmailChange,
            label = { Text("Email") },
            singleLine = true,
            shape = RoundedCornerShape(24.dp),
            colors = authFieldColors(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = password,
            onValueChange = onPasswordChange,
            label = { Text("Password") },
            singleLine = true,
            shape = RoundedCornerShape(24.dp),
            colors = authFieldColors(),
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            modifier = Modifier.fillMaxWidth()
        )

        if (uiState is LoginUiState.Error) {
            Text(text = uiState.message, color = MaterialTheme.colorScheme.error)
        }

        PillButton(
            text = "Login",
            onClick = onLoginClick,
            enabled = uiState !is LoginUiState.Loading,
            loading = uiState is LoginUiState.Loading,
            containerColor = AccentYellow,
            contentColor = OnAccent,
            modifier = Modifier.fillMaxWidth()
        )

        TextButton(
            onClick = onRegisterClick,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Don't have an account? Register", color = AccentYellow)
        }
    }
}

@Composable
internal fun authFieldColors() = OutlinedTextFieldDefaults.colors(
    focusedBorderColor = AccentYellow,
    unfocusedBorderColor = OnBlackMuted,
    focusedLabelColor = AccentYellow,
    unfocusedLabelColor = OnBlackMuted,
    cursorColor = AccentYellow,
    focusedTextColor = OnBlack,
    unfocusedTextColor = OnBlack,
)

@Preview(showBackground = true, backgroundColor = 0xFF0A0A0A)
@Composable
fun LoginScreenPreview() {
    MyNotesApplicationTheme {
        LoginScreenContent(
            email = "",
            password = "",
            onEmailChange = {},
            onPasswordChange = {},
            uiState = LoginUiState.Idle,
            onLoginClick = {},
            onRegisterClick = {}
        )
    }
}

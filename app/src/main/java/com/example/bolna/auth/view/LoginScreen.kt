package com.example.bolna.auth.view

import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.*
import androidx.compose.ui.unit.*
import com.example.bolna.auth.viewModel.LoginUiState


data class LoginUiActions(
    val onUsernameChange: (String) -> Unit,
    val onPasswordChange: (String) -> Unit,
    val onLoginClick: () -> Unit,
    val onGoogleClick: () -> Unit,
    val onRegisterClick: () -> Unit
)

@Composable
fun LoginScreen(
    state: LoginUiState,
    actions: LoginUiActions
) {
    var showPassword by remember { mutableStateOf(false) }
    val focusManager = LocalFocusManager.current
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .pointerInput(Unit) {
                detectTapGestures(onTap = {
                    focusManager.clearFocus()
                })
            }
            .verticalScroll(scrollState)
            .padding(24.dp)
            .imePadding(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Wanna Bolna", style = MaterialTheme.typography.headlineMedium)
        Spacer(Modifier.height(32.dp))

        OutlinedTextField(
            value = state.username,
            onValueChange = actions.onUsernameChange,
            label = { Text("Username") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(Modifier.height(12.dp))

        OutlinedTextField(
            value = state.password,
            onValueChange = actions.onPasswordChange,
            label = { Text("Password") },
            singleLine = true,
            visualTransformation =
                if (showPassword) VisualTransformation.None else PasswordVisualTransformation(),
            trailingIcon = {
                TextButton(onClick = { showPassword = !showPassword }) {
                    Text(if (showPassword) "Hide" else "Show")
                }
            },
            modifier = Modifier.fillMaxWidth()
        )

        state.error?.let {
            Spacer(Modifier.height(8.dp))
            Text(it, color = MaterialTheme.colorScheme.error)
        }

        Spacer(Modifier.height(24.dp))
        Button(
            onClick = actions.onLoginClick,
            enabled = !state.isLoading,
            modifier = Modifier.fillMaxWidth()
        ) {
            if (state.isLoading) CircularProgressIndicator(Modifier.size(20.dp), strokeWidth = 2.dp)
            else Text("Sign in")
        }

        Spacer(Modifier.height(12.dp))
        OutlinedButton(
            onClick = actions.onGoogleClick,
            enabled = !state.isLoading,
            modifier = Modifier.fillMaxWidth()) {
            Text("Sign in with Google")
        }

        Spacer(Modifier.height(16.dp))
        TextButton(
            enabled = !state.isLoading,
            onClick = actions.onRegisterClick) {
            Text("New here? Register")
        }
    }
}
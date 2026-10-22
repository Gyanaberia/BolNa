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
import com.example.bolna.auth.viewModel.RegisterUiState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff

data class RegisterUiActions(
    val onUsernameChange: (String) -> Unit,
    val onPasswordChange: (String) -> Unit,
    val onEmailChange: (String) -> Unit,
    val onMobileNumberChange: (String) -> Unit,
    val onLoginClick: () -> Unit,
    val onGoogleClick: () -> Unit,
    val onRegisterClick: () -> Unit
)

@Composable
fun RegisterScreen(
    state: RegisterUiState,
    actions: RegisterUiActions
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
        Text("Hop On! Bolna", style = MaterialTheme.typography.headlineMedium)
        Spacer(Modifier.height(32.dp))

        //Username
        OutlinedTextField(
            value = state.username,
            onValueChange = actions.onUsernameChange,
            label = { Text("Username") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(Modifier.height(12.dp))

        //E-mail
        OutlinedTextField(
            value = state.email,
            onValueChange = actions.onEmailChange,
            label = { Text("Email") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(Modifier.height(12.dp))

        //Number
        OutlinedTextField(
            value = state.mobileNumber,
            onValueChange = actions.onMobileNumberChange,
            label = { Text("Mobile") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth(),
        )
        Spacer(Modifier.height(12.dp))

        //Password
        OutlinedTextField(
            value = state.password,
            onValueChange = actions.onPasswordChange,
            label = { Text("Password") },
            singleLine = true,
            visualTransformation =
                if (showPassword) VisualTransformation.None else PasswordVisualTransformation(),
            trailingIcon = {
                val image = if (showPassword) {
                    Icons.Filled.Visibility
                } else {
                    Icons.Filled.VisibilityOff
                }

                val description = if (showPassword) "Hide password" else "Show password"

                IconButton(onClick = { showPassword = !showPassword }) {
                    Icon(imageVector = image, contentDescription = description)
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
            onClick = actions.onRegisterClick,
            enabled = !state.isLoading,
            modifier = Modifier.fillMaxWidth()
        ) {
            if (state.isLoading) CircularProgressIndicator(Modifier.size(20.dp), strokeWidth = 2.dp)
            else Text("Register")
        }

        Spacer(Modifier.height(12.dp))
        OutlinedButton(onClick = actions.onGoogleClick,
            enabled = !state.isLoading,
            modifier = Modifier.fillMaxWidth()) {
            Text("Sign in with Google")
        }

        Spacer(Modifier.height(16.dp))
        TextButton(onClick = actions.onLoginClick,
                enabled = !state.isLoading,
            ) {
            Text("Already have an account? Sign In Here")
        }
    }
}
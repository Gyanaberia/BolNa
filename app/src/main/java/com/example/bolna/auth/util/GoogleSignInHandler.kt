package com.example.bolna.auth.util

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.platform.LocalContext
import androidx.credentials.exceptions.GetCredentialCancellationException
import kotlinx.coroutines.launch

@Composable
fun rememberGoogleSignIn(
    onToken: (String) -> Unit,
    onError: (String) -> Unit
): () -> Unit {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val client = remember { GoogleAuthClient() }

    return {
        scope.launch {
            client.signIn(context)
                .onSuccess { token -> onToken(token) }
                .onFailure { e ->
                    // User just closed the popup: not an error worth showing
                    if (e !is GetCredentialCancellationException) {
                        Log.e("GoogleAuth", "Google sign-in failed", e)
                        onError("Google sign-in failed")
                    }
                }
        }
    }
}
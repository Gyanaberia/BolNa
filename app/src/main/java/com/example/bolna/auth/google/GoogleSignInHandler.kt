package com.example.bolna.auth.google

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.platform.LocalContext
import androidx.credentials.exceptions.GetCredentialCancellationException
import kotlinx.coroutines.launch

/**
 * Returns a click listener callback for explicit "Sign in with Google" button taps.
 * Displays the Credential Manager bottom sheet listing all Google accounts on the device.
 */
@Composable
fun rememberGoogleSignIn(
    onToken: (String) -> Unit,
    onError: (String) -> Unit,
): () -> Unit {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val client = remember { GoogleAuthClient() }

    return {
        scope.launch {
            client.signInWithButton(context)
                .onSuccess { token ->
                    Log.d("GoogleAuth", "Google sign-in succeeded via button click.")
                    onToken(token)
                }
                .onFailure { e ->
                    Log.e("GoogleAuth", "Google sign-in failed: [${e::class.java.simpleName}] ${e.message}", e)
                    if ((e is GetCredentialCancellationException) && isUserCancellation(e)) {
                        Log.d("GoogleAuth", "User cancelled Google sign-in UI picker.")
                    } else {
                        onError(e.message ?: "Google sign-in failed")
                    }
                }
        }
    }
}

/**
 * Automatically checks and prompts returning users with Google Sign-In when the screen loads.
 * Fails quietly if no authorized account exists or if the user cancels.
 */
@Composable
fun AutoGoogleSignIn(
    onToken: (String) -> Unit,
) {
    val context = LocalContext.current
    val client = remember { GoogleAuthClient() }

    LaunchedEffect(Unit) {
        client.signInAuto(context)
            .onSuccess { token ->
                Log.d("GoogleAuth", "Auto Google sign-in succeeded.")
                onToken(token)
            }
            .onFailure { e ->
                // Auto sign-in fails quietly for non-authorized users or cancellations
                Log.d("GoogleAuth", "Auto Google sign-in skipped: [${e::class.java.simpleName}] ${e.message}")
            }
    }
}

package com.example.bolna.auth.google

import android.annotation.SuppressLint
import android.content.Context
import android.content.MutableContextWrapper
import android.util.Log
import androidx.credentials.ClearCredentialStateRequest
import androidx.credentials.CredentialManager
import androidx.credentials.CustomCredential
import androidx.credentials.GetCredentialRequest
import androidx.credentials.GetCredentialResponse
import androidx.credentials.exceptions.GetCredentialCancellationException
import androidx.credentials.exceptions.GetCredentialException
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GetSignInWithGoogleOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.android.libraries.identity.googleid.GoogleIdTokenParsingException

/**
 * Helper to determine if a [GetCredentialCancellationException] was caused by a manual user action
 * (e.g. user dismissed or backed out of the picker) versus a system/GMS error (such as code 16 "Account reauth failed").
 */
fun isUserCancellation(e: Throwable): Boolean {
    if (e !is GetCredentialCancellationException) return false
    val message = e.message ?: ""
    return !message.contains("16") && !message.contains("reauth", ignoreCase = true)
}

/**
 * Client for handling Google Sign-In authentication flows using Android's
 * [CredentialManager] API and Google Identity services.
 */
class GoogleAuthClient {

    /**
     * Automatic Sign-In flow (used on screen load).
     * Attempts auto-select for previously authorized accounts seamlessly.
     * Fails quietly if no authorized account exists on the device.
     *
     * @param activityContext The [Context] required to host the Credential Manager UI.
     * @return A [Result] containing the Google ID token [String] on success, or a failure [Result].
     */
    @SuppressLint("CredManMutableContext")
    suspend fun signInAuto(activityContext: Context): Result<String> {
        val credentialManager = CredentialManager.create(activityContext)
        val mutableContext = MutableContextWrapper(activityContext)

        val googleIdOption = GetGoogleIdOption.Builder()
            .setFilterByAuthorizedAccounts(filterByAuthorizedAccounts = true)
            .setServerClientId(AuthConfig.WEB_CLIENT_ID)
            .setAutoSelectEnabled(autoSelectEnabled = true)
            .build()

        val request = GetCredentialRequest.Builder()
            .addCredentialOption(googleIdOption)
            .build()

        return try {
            val result = credentialManager.getCredential(mutableContext, request)
            processCredentialResult(result)
        } catch (e: GetCredentialException) {
            Result.failure(e)
        } catch (e: GoogleIdTokenParsingException) {
            Result.failure(e)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /**
     * Explicit Sign-In flow (used when the user taps the "Sign in with Google" button).
     * Uses [GetSignInWithGoogleOption] (the official button option) to display the account picker,
     * falling back to [GetGoogleIdOption] and clearing state if GMS re-auth or other credential errors occur.
     *
     * @param activityContext The [Context] required to host the Credential Manager UI.
     * @return A [Result] containing the Google ID token [String] on success, or a failure [Result].
     */
    @SuppressLint("CredManMutableContext")
    suspend fun signInWithButton(activityContext: Context): Result<String> {
        val credentialManager = CredentialManager.create(activityContext)
        val mutableContext = MutableContextWrapper(activityContext)

        val signInWithGoogleOption = GetSignInWithGoogleOption.Builder(
            serverClientId = AuthConfig.WEB_CLIENT_ID
        ).build()

        val request = GetCredentialRequest.Builder()
            .addCredentialOption(signInWithGoogleOption)
            .build()

        return try {
            val result = credentialManager.getCredential(mutableContext, request)
            processCredentialResult(result)
        } catch (e: GetCredentialCancellationException) {
            if (isUserCancellation(e)) {
                // User explicitly cancelled the picker prompt; do not retry
                Result.failure(e)
            } else {
                Log.w(
                    "GoogleAuth",
                    "signInWithButton failed with non-user cancellation [${e.message}]. Clearing state and retrying via fallback.",
                    e
                )
                tryClearStateAndFallback(credentialManager, mutableContext)
            }
        } catch (e: GetCredentialException) {
            Log.w(
                "GoogleAuth",
                "signInWithButton failed [${e.message}]. Clearing state and retrying via fallback.",
                e
            )
            tryClearStateAndFallback(credentialManager, mutableContext)
        } catch (e: GoogleIdTokenParsingException) {
            Result.failure(e)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    private suspend fun tryClearStateAndFallback(
        credentialManager: CredentialManager,
        context: MutableContextWrapper,
    ): Result<String> {
        try {
            credentialManager.clearCredentialState(ClearCredentialStateRequest())
        } catch (e: Exception) {
            Log.w("GoogleAuth", "Failed to clear credential state during fallback: ${e.message}")
        }
        return signInWithGoogleIdOptionFallback(credentialManager, context)
    }

    private suspend fun signInWithGoogleIdOptionFallback(
        credentialManager: CredentialManager,
        context: MutableContextWrapper,
    ): Result<String> {
        val googleIdOption = GetGoogleIdOption.Builder()
            .setFilterByAuthorizedAccounts(filterByAuthorizedAccounts = false)
            .setServerClientId(AuthConfig.WEB_CLIENT_ID)
            .setAutoSelectEnabled(autoSelectEnabled = false)
            .build()

        val request = GetCredentialRequest.Builder()
            .addCredentialOption(googleIdOption)
            .build()

        return try {
            val result = credentialManager.getCredential(context, request)
            processCredentialResult(result)
        } catch (e: GetCredentialCancellationException) {
            if (isUserCancellation(e)) {
                Result.failure(e)
            } else {
                Log.e("GoogleAuth", "Fallback sign-in also failed with non-user cancellation: ${e.message}", e)
                Result.failure(e)
            }
        } catch (e: GetCredentialException) {
            Result.failure(e)
        } catch (e: GoogleIdTokenParsingException) {
            Result.failure(e)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /**
     * Clears the credential state in [CredentialManager] upon user sign-out.
     *
     * Calling this ensures that any active credential session stored by credential providers
     * (e.g. Google Play Services) is cleared, giving the user full options to pick an account
     * the next time they initiate sign-in.
     */
    suspend fun signOut(context: Context) {
        val credentialManager = CredentialManager.create(context)
        try {
            credentialManager.clearCredentialState(ClearCredentialStateRequest())
            Log.d("GoogleAuth", "Successfully cleared CredentialManager state on sign-out.")
        } catch (e: Exception) {
            Log.e("GoogleAuth", "Failed to clear CredentialManager state on sign-out.", e)
        }
    }

    private fun processCredentialResult(result: GetCredentialResponse): Result<String> {
        val credential = result.credential
        return if ((credential is CustomCredential) &&
            (credential.type == GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL)
        ) {
            val idToken = GoogleIdTokenCredential.createFrom(credential.data).idToken
            Result.success(idToken)
        } else {
            Result.failure(IllegalStateException("Unexpected credential type: ${credential.type}"))
        }
    }
}

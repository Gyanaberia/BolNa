package com.example.bolna.auth.util

import android.content.Context
import android.content.MutableContextWrapper
import androidx.credentials.CredentialManager
import androidx.credentials.CustomCredential
import androidx.credentials.GetCredentialRequest
import androidx.credentials.exceptions.GetCredentialException
import com.google.android.libraries.identity.googleid.GetSignInWithGoogleOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.android.libraries.identity.googleid.GoogleIdTokenParsingException

/**
 * Client for handling Google Sign-In authentication flows using Android's
 * [CredentialManager] API and Google Identity services.
 *
 * Constructs sign-in requests using [GetSignInWithGoogleOption] configured with
 * [AuthConfig.WEB_CLIENT_ID] and retrieves a Google ID token upon successful user authentication.
 */
class GoogleAuthClient {

    /**
     * Initiates the Google Sign-In flow using [CredentialManager].
     *
     * Displays the Credential Manager UI prompt to the user to sign in with their Google account.
     * Upon successful authentication, returns the Google ID token which can be used to authenticate
     * with backend services.
     *
     * @param activityContext The [Context] (typically an Activity context) required to host the Credential Manager UI.
     * @return A [Result] containing the Google ID token [String] on success, or a failure [Result] containing
     * the caught exception (e.g. [GetCredentialException] if cancelled/failed, [GoogleIdTokenParsingException] if token parsing fails,
     * or [IllegalStateException] if an unexpected credential type is returned).
     */
    suspend fun signIn(activityContext: Context): Result<String> {
        //CredentialManager doesn't just fetch data; it UI-pops up a system sheet from the bottom of the screen (asking the user to choose an account or scan a fingerprint).
        //Your app code only has permission to draw inside its own white or black box on the screen.
        // It does not have permission to draw system-level pop-ups.
        // By passing the context (specifically an Activity context), you are saying:
        // "Android System, here is my active screen window. Please overlay your secure login prompt right on top of it."
        val credentialManager = CredentialManager.create(activityContext)
        //It tells the Android system, "When you show the account picker, I specifically want Google accounts."
        // It passes your WEB_CLIENT_ID (a unique ID from your Google Developer Console) so Google knows which backend server this login belongs to.
        val option = GetSignInWithGoogleOption.Builder(AuthConfig.WEB_CLIENT_ID).build()

        //• An app can ask for multiple types of login at once (like Passkeys, saved passwords, and Google Sign-In).
        // Here, you are building the overall request and adding the Google option you just created above into it.
        val request = GetCredentialRequest.Builder()
            .addCredentialOption(option)
            .build()

        return try {
            val result = credentialManager.getCredential(MutableContextWrapper(activityContext), request)
            val credential = result.credential

            if (credential is CustomCredential &&
                credential.type == GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL
            ) {
                val idToken = GoogleIdTokenCredential.createFrom(credential.data).idToken
                Result.success(idToken)
            } else {
                Result.failure(IllegalStateException("Unexpected credential type"))
            }
        } catch (e: GetCredentialException) {
            Result.failure(e)
        } catch (e: GoogleIdTokenParsingException) {
            Result.failure(e)
        }
    }
}
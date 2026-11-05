package com.example.bolna.auth.data.service

import android.content.Context
import android.util.Log
import androidx.credentials.ClearCredentialStateRequest
import androidx.credentials.CredentialManager
import com.example.bolna.auth.data.dto.AuthResponse
import com.example.bolna.auth.data.dto.GoogleOAuthRequest
import com.example.bolna.auth.data.dto.LoginRequest
import com.example.bolna.auth.data.dto.RegisterRequest
import com.example.bolna.core.TokenStorage
import com.google.gson.Gson
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import okio.IOException
import retrofit2.Response
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthRepository @Inject constructor(
    private val authService: AuthService,
    private val tokenStorage: TokenStorage,
    @param:ApplicationContext private val context: Context,
) {
    private val gson = Gson()

    suspend fun login(username: String, password: String) =
        authenticate {
            authService.loginUser(
                LoginRequest(username, password)
            )
        }

    suspend fun register(username: String, email: String, number: String, password: String) =
        authenticate {
            authService.registerUser(
                RegisterRequest(username, email, number, password)
            )
        }

    suspend fun loginWithGoogle(idToken: String) =
        authenticate {
            authService.googleOAuthLoginUser(
                GoogleOAuthRequest(idToken)
            )
        }

    private suspend fun authenticate(
        call: suspend () -> Response<AuthResponse>
    ): Result<AuthResponse> {
        try {
            val response = call()
            val body = response.body()
            if (response.isSuccessful && body != null) {
                tokenStorage.saveToken(body.token)
                return Result.success(body)
            } else {
                return Result.failure(Exception(parseErrorResponse(response.errorBody()?.string())))
            }
        } catch (e: IOException) {
            return Result.failure(e)
        }
    }

    suspend fun logout() {
        tokenStorage.clearToken()
        try {
            val credentialManager = CredentialManager.create(context)
            credentialManager.clearCredentialState(ClearCredentialStateRequest())
            Log.d("AuthRepository", "Cleared CredentialManager state on logout")
        } catch (e: Exception) {
            Log.e("AuthRepository", "Failed to clear CredentialManager state on logout", e)
        }
    }

    private fun parseErrorResponse(errorBodyString: String?): String {
        if (errorBodyString.isNullOrBlank()) return "API failed"
        return try {
            val errorResponse = gson.fromJson(errorBodyString, AuthResponse::class.java)
            errorResponse?.message ?: "API failed"
        } catch (_: Exception) {
            print("exception occurred in registration")
            "API failed"
        }
    }

    val isLoggedIn: Flow<Boolean> = tokenStorage.tokenFlow.map { token -> !token.isNullOrBlank() }
}

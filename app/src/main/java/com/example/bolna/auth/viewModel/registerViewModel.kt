package com.example.bolna.auth.viewModel

import android.view.inputmethod.InputMethodManager
import androidx.core.content.ContextCompat.getSystemService
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bolna.auth.model.AuthResponse
import com.example.bolna.auth.model.GoogleOAuthRequest
import com.example.bolna.auth.model.LoginRequest
import com.example.bolna.auth.model.RegisterRequest
import com.example.bolna.auth.util.AuthApi
import com.google.gson.Gson
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

// feature/auth/LoginViewModel.kt
data class RegisterUiState(
    val username: String = "",
    val password: String = "",
    val email: String = "",
    val mobileNumber: String = "",
    val isLoading: Boolean = false,
    val error: String? = null
)

class RegisterViewModel : ViewModel() {
    private val _state = MutableStateFlow(RegisterUiState())
    val state: StateFlow<RegisterUiState> = _state.asStateFlow()

    private val gson = Gson()
    fun onUsernameChange(v: String) = _state.update { it.copy(username = v, error = null) }
    fun onPasswordChange(v: String) = _state.update { it.copy(password = v, error = null) }
    fun onEmailChange(v: String) = _state.update { it.copy(email = v, error = null) }
    fun onMobileChange(v: String) = _state.update { it.copy(mobileNumber = v, error = null) }

    fun register() {
        val s = _state.value
        if (s.username.isBlank() || s.password.isBlank()) {
            _state.update { it.copy(error = "Enter username and password") }
            return
        }
        _state.update { it.copy(isLoading = true) }

        viewModelScope.launch {
            try {
                val response = AuthApi.retrofitService.registerUser(
                    RegisterRequest(userName = s.username, email = s.email, mobileNumber = s.mobileNumber, password = s.password)
                )

                if (response.isSuccessful && response.body() != null) {
                    val authResponse = response.body()
                    _state.update { it.copy(isLoading = false) }
                    // TODO: Store token (authResponse.token) and route to dashboard page
                } else {
                    val errorMessage = parseErrorResponse(response.errorBody()?.string())
                    _state.update { it.copy(isLoading = false, error = errorMessage) }
                }
            } catch (e: Exception) {
                _state.update {
                    it.copy(
                        isLoading = false,
                        error = e.localizedMessage ?: "Network connection failed"
                    )
                }
            }
        }
    }

    fun loginWithGoogle(idToken: String) {
        _state.update { it.copy(isLoading = true, error = null) }

        viewModelScope.launch {
            try {
                val response = AuthApi.retrofitService.googleOAuthLoginUser(
                    GoogleOAuthRequest(token = idToken)
                )

                if (response.isSuccessful && response.body() != null) {
                    val authResponse = response.body()!!
                    _state.update { it.copy(isLoading = false, error = authResponse.token) }
                    // TODO: Store token (authResponse.token) and route to dashboard page

                } else {
                    val errorMessage = parseErrorResponse(response.errorBody()?.string())
                    _state.update { it.copy(isLoading = false, error = errorMessage) }
                }
            } catch (e: Exception) {
                _state.update {
                    it.copy(
                        isLoading = false,
                        error = e.localizedMessage ?: "Google sign-in failed"
                    )
                }
            }
        }
    }
    fun showError(message: String) = _state.update { it.copy(error = message) }

    private fun parseErrorResponse(errorBodyString: String?): String {
        if (errorBodyString.isNullOrBlank()) return "Registration failed"
        return try {
            val errorResponse = gson.fromJson(errorBodyString, AuthResponse::class.java)
            errorResponse?.message ?: "Registration failed"
        } catch (_: Exception) {
            print("exception occurred in registration")
            "Registration failed"
        }
    }
}
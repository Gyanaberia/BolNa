package com.example.bolna.auth.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bolna.auth.model.AuthResponse
import com.example.bolna.auth.model.GoogleOAuthRequest
import com.example.bolna.auth.model.LoginRequest
import com.example.bolna.auth.util.AuthApi
import com.google.gson.Gson
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class LoginUiState(
    val username: String = "",
    val password: String = "",
    val isLoading: Boolean = false,
    val error: String? = null
)

class LoginViewModel : ViewModel() {
    private val _state = MutableStateFlow(LoginUiState())
    val state: StateFlow<LoginUiState> = _state.asStateFlow()

    private val gson = Gson()

    fun onUsernameChange(v: String) = _state.update { it.copy(username = v, error = null) }
    fun onPasswordChange(v: String) = _state.update { it.copy(password = v, error = null) }

    fun login() {
        val s = _state.value
        if (s.username.isBlank() || s.password.isBlank()) {
            _state.update { it.copy(error = "Enter username and password") }
            return
        }

        _state.update { it.copy(isLoading = true, error = null) }

        viewModelScope.launch {
            try {
                val response = AuthApi.retrofitService.loginUser(
                    LoginRequest(userName = s.username, password = s.password)
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
        if (errorBodyString.isNullOrBlank()) return "Authentication failed"
        return try {
            val errorResponse = gson.fromJson(errorBodyString, AuthResponse::class.java)
            errorResponse?.message ?: "Authentication failed"
        } catch (_: Exception) {
            "Authentication failed"
        }
    }
}

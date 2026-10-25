package com.example.bolna.auth.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bolna.auth.data.dto.AuthResponse
import com.example.bolna.auth.data.dto.GoogleOAuthRequest
import com.example.bolna.auth.data.service.AuthRepository
import com.google.gson.Gson
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class LoginUiState(
    val username: String = "",
    val password: String = "",
    val isLoading: Boolean = false,
    val error: String? = null,
    val isAuthenticated: Boolean = false
)

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {
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

        if(s.password.length<8){
            _state.update { it.copy(error="Incorrect UserName or Password") }
        }
        _state.update { it.copy(isLoading = true, error = null) }

        viewModelScope.launch {
                authRepository.login(s.username, s.password)
                    .onSuccess {
                        _state.update { it.copy(isLoading = false, isAuthenticated = true) }
                    }
                    .onFailure{
                            e ->
                        _state.update { it.copy(isLoading = false, error = e.message ?: "Something went wrong") }

                    }

            }
        }

    fun loginWithGoogle(idToken: String) {
        _state.update { it.copy(isLoading = true, error = null) }

        viewModelScope.launch {
            authRepository.loginWithGoogle(idToken)
                .onSuccess {
                    _state.update { it.copy(isLoading = false, isAuthenticated = true) }
                }
                .onFailure{
                        e ->
                    _state.update { it.copy(isLoading = false, error = e.message ?: "Something went wrong") }

                }
        }
    }

    fun showError(message: String) = _state.update { it.copy(error = message) }

}

package com.example.bolna.auth.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bolna.auth.data.service.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

// feature/auth/LoginViewModel.kt
data class RegisterUiState(
    val username: String = "",
    val password: String = "",
    val email: String = "",
    val mobileNumber: String = "",
    val isLoading: Boolean = false,
    val error: String? = null,
    val isAuthenticated: Boolean = false
)

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val authRepository: AuthRepository

): ViewModel() {
    private val _state = MutableStateFlow(RegisterUiState())
    val state: StateFlow<RegisterUiState> = _state.asStateFlow()

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
        if(s.password.length<8 || s.password.length>20){
            _state.update { it.copy(error = "Password should be 8 and 20 long") }
            return
        }
        _state.update { it.copy(isLoading = true) }
        viewModelScope.launch {
            authRepository.register(s.username,s.email,s.mobileNumber, s.password)
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
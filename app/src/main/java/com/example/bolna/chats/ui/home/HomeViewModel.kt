package com.example.bolna.chats.ui.home

import android.R
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bolna.auth.data.service.AuthRepository
import com.example.bolna.auth.viewModel.RegisterUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class HomeUiState(
    var isLoggedOut: Boolean = false
)
@HiltViewModel
class HomeViewModel @Inject constructor(
    private val authRepository: AuthRepository
): ViewModel() {
    private val _state = MutableStateFlow(HomeUiState())
    val state: StateFlow<HomeUiState> = _state.asStateFlow()

    fun loadProfileAtStart(){

    }
    fun viewProfile(){

    }
    fun viewChat(){

    }

    fun logOut(){
        viewModelScope.launch {
            authRepository.logout()
            _state.update { it.copy(isLoggedOut = true) }
        }
    }

}
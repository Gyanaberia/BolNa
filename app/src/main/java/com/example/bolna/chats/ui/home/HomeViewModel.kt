package com.example.bolna.chats.ui.home


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bolna.auth.data.service.AuthRepository
import com.example.bolna.chats.model.ChatRoom
import com.example.bolna.chats.model.RoomType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.Instant
import javax.inject.Inject

data class HomeUiState(
    val rooms: List<ChatRoom> = emptyList(),
    val searchQuery: String = "",
    val isLoading: Boolean = false,
    val error: String? = null,
    val isLoggedOut: Boolean = false,
    val userName: String = "Your name",       // temporary until user info is loaded
    val userHandle: String = "@username",      // temporary
) {
    // What the list actually shows: filtered by search, newest first
    val visibleRooms: List<ChatRoom>
        get() = rooms
            .filter { it.name.contains(searchQuery, ignoreCase = true) }
            .sortedByDescending { it.lastActivityAt }
}


@HiltViewModel
class HomeViewModel @Inject constructor(
    private val authRepository: AuthRepository
): ViewModel() {
    private val _state = MutableStateFlow(HomeUiState())
    val state: StateFlow<HomeUiState> = _state.asStateFlow()
    private fun fakeRooms() = listOf(
        ChatRoom("1", "Family", RoomType.GROUP,"Gyan" ,Instant.parse("2026-09-25T18:47:27Z"),null, "Dinner at 8?", Instant.parse("2026-10-25T18:47:27Z"), 3),
        ChatRoom("2", "Ravi", RoomType.DM, "Ajinkya" ,Instant.parse("2026-09-25T18:47:27Z"),null, "See you tomorrow", Instant.parse("2026-10-24T09:10:00Z"), 0),
        ChatRoom("3", "College friends", RoomType.GROUP, "Rando" ,Instant.parse("2026-09-25T18:47:27Z"),null, null, Instant.parse("2026-10-20T21:30:00Z"), 12),
    )
    init {
        //Update it to get rooms from repo
        _state.update { it.copy(rooms = fakeRooms()) }
    }

    fun onSearchChange(query: String) =
        _state.update { it.copy(searchQuery = query) }

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
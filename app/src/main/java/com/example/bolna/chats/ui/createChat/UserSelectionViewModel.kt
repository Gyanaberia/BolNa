package com.example.bolna.chats.ui.createChat

import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.bolna.chats.model.RoomType
import com.example.bolna.chats.model.UserInfo
import com.example.bolna.chats.model.fakeUsers
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

data class UserSelectionState(
    val allUsers: List<UserInfo> = emptyList(),
    val selectedUsers: Set<UserInfo> = emptySet(),
    val groupName:String = "",
    val searchQuery: String = "",
    val error: String? = null,
    val screenType: RoomType = RoomType.DM,
    val isSearchOn: Boolean = false
) {
    val matchingUsers: List<UserInfo>
        get() = allUsers
            .filter { it.name.contains(searchQuery, ignoreCase = true) }
            .sortedByDescending { it.name }
}

@HiltViewModel
class UserSelectionViewModel @Inject constructor() : ViewModel() {
    private val _state = MutableStateFlow(UserSelectionState())
    val state: StateFlow<UserSelectionState> = _state.asStateFlow()

    init {
        _state.update { it.copy(allUsers = fakeUsers()) }
    }

    fun setCreationType(type: RoomType) {
        _state.update { it.copy(screenType = type) }
    }

    fun toggleSearch() {
        _state.update { it.copy(isSearchOn = !it.isSearchOn) }
    }

    fun searchUsers(input: String) {
        _state.update { it.copy(searchQuery = input) }
    }

    fun toggleUser(user: UserInfo) {
        _state.update { currentState ->
            val newSelectedUsers = if (currentState.selectedUsers.contains(user)) {
                currentState.selectedUsers - user
            } else {
                currentState.selectedUsers + user
            }

            Log.i("ToggleUser", "selectedUsers count: ${newSelectedUsers.size}")

            currentState.copy(
                selectedUsers = newSelectedUsers,
            )
        }
    }

    // Remove Users
    fun removeUserFromList(user: UserInfo) {
        _state.update { currentState ->
            val newSetOfUsers = currentState.selectedUsers - user
            currentState.copy(selectedUsers = newSetOfUsers)
        }
    }

    fun removeLastUserAndGoBack(call:()->Unit){
        _state.update { it.copy(selectedUsers = emptySet()) }
        call()
    }

    // Set GroupName
    fun setGroupName(groupName: String) {
        _state.update { it.copy(groupName = groupName) }
    }
}

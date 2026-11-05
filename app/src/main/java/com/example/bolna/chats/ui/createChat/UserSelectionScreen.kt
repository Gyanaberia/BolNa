package com.example.bolna.chats.ui.createChat

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.activity.compose.BackHandler
import androidx.compose.runtime.setValue
import com.example.bolna.chats.model.RoomType
import com.example.bolna.chats.model.UserInfo
import com.example.bolna.chats.ui.components.UserItemMini
import com.example.bolna.chats.ui.components.UserItemRow
import com.example.bolna.core.ui.components.CustomAlertBox

data class UserSelectionActions(
    val onSearchChange: (String) -> Unit,
    val onUserSelect: (UserInfo) -> Unit,
    val gotoNextScreen: () -> Unit,
    val gotoHomeScreen: () -> Unit,
    val toggleSearch: () -> Unit,
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserSelectionScreen(
    state: UserSelectionState,
    actions: UserSelectionActions
) {
    var showDiscardDialog by remember { mutableStateOf(false) }
    val hasSelection = state.selectedUsers.isNotEmpty()

    // The ONE place that decides what "back" means
    val onBackRequested:()->Unit = {
        if (hasSelection) showDiscardDialog = true else actions.gotoHomeScreen()
    }

    // Catches system back (gesture / nav key). Disabled when nothing is
    // selected, so back behaves normally then.
    BackHandler(enabled = hasSelection) { showDiscardDialog = true }

    Scaffold(
        topBar = {
            AnimatedContent(
                targetState = state.isSearchOn,
                transitionSpec = {
                    if (targetState) {
                        (slideInHorizontally(animationSpec = tween(250)) { width -> width } + fadeIn(tween(250)))
                            .togetherWith(slideOutHorizontally(animationSpec = tween(200)) { width -> -width } + fadeOut(tween(200)))
                    } else {
                        (slideInHorizontally(animationSpec = tween(250)) { width -> -width } + fadeIn(tween(250)))
                            .togetherWith(slideOutHorizontally(animationSpec = tween(200)) { width -> width } + fadeOut(tween(200)))
                    }
                },
                label = "TopAppBarSearchTransition"
            ) { searching ->
                TopAppBar(
                    colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.LightGray),
                    title = {
                        if (searching) {
                            OutlinedTextField(
                                value = state.searchQuery,
                                onValueChange = actions.onSearchChange,
                                placeholder = { Text("Search users...") },
                                leadingIcon = {
                                    Icon(
                                        Icons.Default.Search,
                                        contentDescription = null
                                    )
                                },
                                trailingIcon = {
                                    if (state.searchQuery.isNotEmpty()) {
                                        IconButton(onClick = { actions.onSearchChange("") }) {
                                            Icon(
                                                Icons.Default.Close,
                                                contentDescription = "Clear Search"
                                            )
                                        }
                                    }
                                },
                                singleLine = true,
                                shape = RoundedCornerShape(28.dp),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 16.dp, vertical = 8.dp)
                            )
                        } else {
                            Column {
                                Text(
                                    text = if (state.screenType == RoomType.DM) "New Chat" else "New Group",
                                    style = MaterialTheme.typography.titleLarge,
                                    fontWeight = FontWeight.SemiBold
                                )
                                if (state.screenType == RoomType.GROUP && state.selectedUsers.isNotEmpty()) {
                                    Text(
                                        text = "${state.selectedUsers.size} selected",
                                        style = MaterialTheme.typography.bodySmall,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                            }
                        }
                    },
                    navigationIcon = {
                        IconButton(onClick = if (searching) actions.toggleSearch else onBackRequested) {
                            Icon(
                                Icons.AutoMirrored.Filled.ArrowBack,
                                modifier = Modifier.size(25.dp),
                                contentDescription = "Go Home"
                            )
                        }
                    },
                    actions = {
                        if (!searching) {
                            IconButton(onClick = actions.toggleSearch) {
                                Icon(
                                    Icons.Default.Search,
                                    modifier = Modifier.size(25.dp),
                                    contentDescription = "Search"
                                )
                            }
                        }
                    }
                )
            }
        },
        floatingActionButton = {
            AnimatedVisibility(
                visible = state.screenType == RoomType.GROUP && state.selectedUsers.isNotEmpty()
            ) {
                FloatingActionButton(
                    onClick = actions.gotoNextScreen,
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary
                ) {
                    Icon(
                        Icons.AutoMirrored.Filled.KeyboardArrowRight,
                        contentDescription = "Next Screen"
                    )
                }
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            // Selected Users Horizontal List with smooth height expansion & fade transition
            AnimatedVisibility(
                visible = state.screenType == RoomType.GROUP && state.selectedUsers.isNotEmpty(),
                enter = expandVertically(
                    animationSpec = spring(
                        dampingRatio = Spring.DampingRatioLowBouncy,
                        stiffness = Spring.StiffnessLow
                    )
                ) + fadeIn(animationSpec = tween(250)),
                exit = shrinkVertically(
                    animationSpec = tween(250)
                ) + fadeOut(animationSpec = tween(200))
            ) {
                LazyRow(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    contentPadding = PaddingValues(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(
                        items = state.selectedUsers.toList(),
                        key = { user -> user.userId }
                    ) { user ->
                        Box(modifier = Modifier.animateItem()) {
                            UserItemMini(
                                user = user,
                                onClick = { actions.onUserSelect(user) }
                            )
                        }
                    }
                }
            }

            // Main User List
            if (state.allUsers.isEmpty()) {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text(
                        if (state.searchQuery.isBlank()) "No users yet" else "No users match your search"
                    )
                }
            } else {
                LazyColumn(Modifier.fillMaxSize()) {
                    items(
                        items = state.matchingUsers,
                        key = { it.userId }
                    ) { user ->
                        val isSelected = state.selectedUsers.contains(user)
                        UserItemRow(
                            user = user,
                            isChecked = isSelected,
                            onClick = { actions.onUserSelect(user) },
                            showCheckbox = state.screenType == RoomType.GROUP
                        )
                    }
                }
            }
        }
    }
    if (showDiscardDialog) {
        CustomAlertBox(
            // adapt these to your composable's real parameter names
            dialogTitle = "Discard group?",
            dialogText = "Your selected users will be cleared.",
            onConfirmation = {
                showDiscardDialog = false
                actions.gotoHomeScreen()
            },
            onDismissRequest = { showDiscardDialog = false }
        )
    }

}

package com.example.bolna.chats.ui.home

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Groups
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SmallFloatingActionButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.unit.dp
import com.example.bolna.core.ui.components.AvatarPlaceholder
import kotlinx.coroutines.launch


data class HomeUiActions(
    val onSearchChange: (String) -> Unit,
    val onRoomClick: (String) -> Unit,
    val onNewChatClick: () -> Unit,
    val onNewDMClick: () -> Unit,
    val onNewGroupClick: () -> Unit,
    val onProfileClick: () -> Unit,
    val onSettingsClick: () -> Unit,
    val onLogOutClick: () -> Unit
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatHomeScreen(state: HomeUiState, actions: HomeUiActions) {
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val focusManager = LocalFocusManager.current

    // Helper: close the drawer, then run the action
    fun closeThen(action: () -> Unit) {
        scope.launch { drawerState.close() }
        action()
    }

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet {
                Column(Modifier.padding(24.dp)) {
                    AvatarPlaceholder(name = state.userName, size = 64.dp)
                    Spacer(Modifier.height(12.dp))
                    Text(state.userName, style = MaterialTheme.typography.titleLarge)
                    Text(state.userHandle, style = MaterialTheme.typography.bodyMedium)
                }
                HorizontalDivider()
                NavigationDrawerItem(
                    label = { Text("Profile") },
                    icon = { Icon(Icons.Default.Person, contentDescription = null) },
                    selected = false,
                    onClick = { closeThen(actions.onProfileClick) }
                )
                NavigationDrawerItem(
                    label = { Text("Settings") },
                    icon = { Icon(Icons.Default.Settings, contentDescription = null) },
                    selected = false,
                    onClick = { closeThen(actions.onSettingsClick) }
                )
                NavigationDrawerItem(
                    label = { Text("Log out") },
                    icon = { Icon(Icons.AutoMirrored.Filled.ExitToApp, contentDescription = null) },
                    selected = false,
                    onClick = { closeThen(actions.onLogOutClick) }
                )
            }
        }
    ) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("Chats") },
                    navigationIcon = {
                        IconButton(onClick = { scope.launch { drawerState.open() } }) {
                            Icon(Icons.Default.Menu, contentDescription = "Menu")
                        }
                    }
                )
            },
            floatingActionButton = {
                Column(
                    horizontalAlignment = Alignment.End,
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    // Popping animation for Speed Dial menu items (DM and Group)
                    AnimatedVisibility(
                        visible = state.isMenuOpen,
                        enter = fadeIn(animationSpec = tween(200)) +
                                slideInVertically(initialOffsetY = { it / 3 }, animationSpec = tween(200)) +
                                scaleIn(transformOrigin = TransformOrigin(1f, 1f), animationSpec = tween(200)),
                        exit = fadeOut(animationSpec = tween(150)) +
                                slideOutVertically(targetOffsetY = { it / 3 }, animationSpec = tween(150)) +
                                scaleOut(transformOrigin = TransformOrigin(1f, 1f), animationSpec = tween(150))
                    ) {
                        Column(
                            horizontalAlignment = Alignment.End,
                            verticalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            // 1. New DM Option
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.End
                            ) {
                                Surface(
                                    shape = RoundedCornerShape(8.dp),
                                    color = MaterialTheme.colorScheme.surfaceContainerHigh,
                                    tonalElevation = 4.dp,
                                    shadowElevation = 2.dp,
                                    modifier = Modifier.clickable { actions.onNewDMClick() }
                                ) {
                                    Text(
                                        text = "New DM",
                                        style = MaterialTheme.typography.labelLarge,
                                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                                    )
                                }
                                Spacer(Modifier.width(12.dp))
                                SmallFloatingActionButton(
                                    onClick = actions.onNewDMClick,
                                    containerColor = MaterialTheme.colorScheme.secondaryContainer,
                                    contentColor = MaterialTheme.colorScheme.onSecondaryContainer
                                ) {
                                    Icon(Icons.Default.Person, contentDescription = "New DM")
                                }
                            }

                            // 2. New Group Option
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.End
                            ) {
                                Surface(
                                    shape = RoundedCornerShape(8.dp),
                                    color = MaterialTheme.colorScheme.surfaceContainerHigh,
                                    tonalElevation = 4.dp,
                                    shadowElevation = 2.dp,
                                    modifier = Modifier.clickable { actions.onNewGroupClick() }
                                ) {
                                    Text(
                                        text = "New Group",
                                        style = MaterialTheme.typography.labelLarge,
                                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                                    )
                                }
                                Spacer(Modifier.width(12.dp))
                                SmallFloatingActionButton(
                                    onClick = actions.onNewGroupClick,
                                    containerColor = MaterialTheme.colorScheme.secondaryContainer,
                                    contentColor = MaterialTheme.colorScheme.onSecondaryContainer
                                ) {
                                    Icon(Icons.Default.Groups, contentDescription = "New Group")
                                }
                            }
                        }
                    }

                    // Rotating Main FAB (+) -> (X)
                    val fabRotation by animateFloatAsState(
                        targetValue = if (state.isMenuOpen) 135f else 0f,
                        animationSpec = tween(durationMillis = 250),
                        label = "FabRotation"
                    )

                    FloatingActionButton(
                        onClick = actions.onNewChatClick,
                        containerColor = if (state.isMenuOpen) MaterialTheme.colorScheme.secondary else MaterialTheme.colorScheme.primary,
                        contentColor = if (state.isMenuOpen) MaterialTheme.colorScheme.onSecondary else MaterialTheme.colorScheme.onPrimary
                    ) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = if (state.isMenuOpen) "Close menu" else "New chat",
                            modifier = Modifier.rotate(fabRotation)
                        )
                    }
                }
            },
            modifier = Modifier.pointerInput(Unit) {
                detectTapGestures(onTap = {
                    focusManager.clearFocus()
                })
            }
        ) { innerPadding ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
            ) {
                // Main Content
                Column(Modifier.fillMaxSize()) {
                    OutlinedTextField(
                        value = state.searchQuery,
                        onValueChange = actions.onSearchChange,
                        placeholder = { Text("Search chats") },
                        leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
                        singleLine = true,
                        shape = RoundedCornerShape(28.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 8.dp)
                    )

                    if (state.visibleRooms.isEmpty()) {
                        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                            Text(
                                if (state.searchQuery.isBlank()) "No chats yet" else "No chats match your search"
                            )
                        }
                    } else {
                        LazyColumn(Modifier.fillMaxSize()) {
                            items(items = state.visibleRooms, key = { it.id }) { room ->
                                ChatRoomItem(room = room, onClick = { actions.onRoomClick(room.id) })
                            }
                        }
                    }
                }

                // Dimmed Overlay Scrim when menu is open
                AnimatedVisibility(
                    visible = state.isMenuOpen,
                    enter = fadeIn(animationSpec = tween(200)),
                    exit = fadeOut(animationSpec = tween(200))
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(Color.Black.copy(alpha = 0.5f))
                            .pointerInput(Unit) {
                                detectTapGestures(onTap = {
                                    actions.onNewChatClick()
                                })
                            }
                    )
                }
            }
        }
    }
}

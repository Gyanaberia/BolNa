package com.example.bolna.chats.ui.createChat

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.DividerDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.bolna.chats.model.UserInfo
import com.example.bolna.chats.ui.components.UserItemMini
import com.example.bolna.core.ui.components.AvatarPlaceholder


data class SetGroupDetailsActions(
    val setGroupName: (String) -> Unit,
    val removeUser: (UserInfo) -> Unit,
    val removeUserAndGoBack: (()-> Unit)->Unit,
    val gotoPreviousScreen: () -> Unit,
    val gotoNextScreen:() -> Unit,
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SetGroupDetailsScreen(
    state: UserSelectionState, actions: SetGroupDetailsActions
){
    var textFocus by remember { mutableStateOf(false) }
    val maxGroupNameLength = 30

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "New Group",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.SemiBold
                    )
                },
                navigationIcon = {
                    IconButton(onClick = actions.gotoPreviousScreen) {
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowBack,
                            modifier = Modifier.size(25.dp),
                            contentDescription = "Go back to user selection Menu"
                        )
                    }
                },
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = actions.gotoNextScreen,
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary
            ) {
                Icon(
                    Icons.Default.Check,
                    contentDescription = "Next Screen"
                )
            }

        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues = innerPadding)
        ){
            Row(
                modifier = Modifier.background(color = Color.White)
                    .padding(start = 10.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                AvatarPlaceholder(name = state.groupName, size = 60.dp)
                Spacer(modifier = Modifier.width(10.dp))
                Column(
                    modifier = Modifier
                        .padding(horizontal = 10.dp),
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Spacer(modifier = Modifier.height(5.dp))
                    Text(
                        text = "New Group",
                        style = MaterialTheme.typography.bodySmall,
                    )
//                    Spacer(modifier = Modifier.height(5.dp))
                    TextField(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable(onClick = {textFocus=!textFocus}),
                        value = state.groupName,
                        onValueChange =
                            {textValue-> if(textValue.length<=maxGroupNameLength)
                            actions.setGroupName(textValue)},
                        singleLine = true,
                        )
//                    Spacer(modifier = Modifier.height(5.dp))
                    HorizontalDivider(Modifier,
                        thickness = if(textFocus) DividerDefaults.Thickness else Dp.Hairline,
                       color= if(textFocus) Color.Green else  DividerDefaults.color)
//                    Spacer(modifier = Modifier.height(5.dp))
                    Text(
                        text = "${state.groupName.length}/${maxGroupNameLength}",
                        style = MaterialTheme.typography.bodySmall,
                        fontWeight = FontWeight.Light,
                        modifier = Modifier.align(Alignment.End)
                    )

                }
            }
            Spacer(modifier = Modifier.height(30.dp))
            FlowRow(
                modifier = Modifier.padding(horizontal = 15.dp).fillMaxWidth(),
                maxItemsInEachRow = 4,
                horizontalArrangement = Arrangement.spacedBy(20.dp),
                verticalArrangement = Arrangement.spacedBy(20.dp)
            ) {
                state.selectedUsers.forEach { user ->
                    UserItemMini(
                        user = user,
                        onClick = {
                            if(state.selectedUsers.size==1)actions.removeUserAndGoBack(actions.gotoPreviousScreen)
                            actions.removeUser(user)
                        }
                    )
                }
            }
        }
    }
}
package com.example.bolna.chats.ui.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp


data class HomeUiActions(
    val onLogOutClick:()->Unit
)
@Composable
fun ChatHomeScreen(
    actions: HomeUiActions,
) {

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Home (chats will go here)")
        Spacer(Modifier.height(16.dp))
        Button(onClick = actions.onLogOutClick) {
            Text("Log out")
        }
    }
}
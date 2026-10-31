package com.example.bolna.chats.ui.home

import android.R
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.CutCornerShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Badge
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.bolna.chats.model.ChatRoom
import com.example.bolna.core.common.formatChatTime
import com.example.bolna.core.ui.components.AvatarPlaceholder

@Composable
fun ChatRoomItem(room: ChatRoom, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(horizontal = 16.dp, vertical = 12.dp),
//            .background(color = Color.LightGray),
//            .border(width = Dp.Hairline, color= Color.Blue, shape = RoundedCornerShape(6.dp)),
        verticalAlignment = Alignment.CenterVertically
    ) {
        AvatarPlaceholder(name = room.name)
        Spacer(Modifier.width(12.dp))

        // weight(1f) = take all the space the other children don't need
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = room.name,
                style = MaterialTheme.typography.titleMedium,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                text = room.lastMessage ?: "No messages yet",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }

        Spacer(Modifier.width(8.dp))
        Column(horizontalAlignment = Alignment.End) {
            Text(
                text = formatChatTime(room.lastActivityAt),
                style = MaterialTheme.typography.labelSmall
            )
            if (room.unreadCount > 0) {
                Spacer(Modifier.height(4.dp))
                Badge { Text(room.unreadCount.toString()) }
            }
        }
    }
}


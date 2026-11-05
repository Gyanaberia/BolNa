package com.example.bolna.chats.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.bolna.chats.model.UserInfo
import com.example.bolna.core.ui.components.AvatarPlaceholder

@Composable
fun UserItemRow(
    user: UserInfo,
    isChecked: Boolean,
    onClick: () -> Unit,
    showCheckbox: Boolean = false,
    showClose:Boolean = false,
) {
    //either both are false, or only one is active
    check((!showCheckbox && !showClose)||(showCheckbox!=showClose))
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        AvatarPlaceholder(name = user.name)
        Spacer(Modifier.width(12.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = user.name,
                style = MaterialTheme.typography.titleMedium,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                text = user.userHandle.ifEmpty { "Hi, I'm using Bolna" },
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }

        if (showCheckbox) {
            Spacer(Modifier.width(8.dp))
            Checkbox(
                checked = isChecked,
                onCheckedChange = null
            )
        }
        else if(showClose){
            Spacer(Modifier.width(8.dp))
            Icon(
                Icons.Default.Close,
                modifier = Modifier.size(20.dp),
                contentDescription = "Remove user",
            )
        }
    }
}

@Composable
fun UserItemMini(
    user: UserInfo,
    size: Dp = 60.dp,
    onClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .padding(horizontal = 5.dp)
            .width(size)
            .clickable(onClick = onClick),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        AvatarPlaceholder(name = user.name, showCross = true, size = size)
        Spacer(Modifier.height(6.dp))
        Text(
            text = user.name,
            overflow = TextOverflow.Ellipsis,
            style = MaterialTheme.typography.bodySmall,
            maxLines = 1
        )
    }
}

package com.example.bolna.chats.data.mapper

import com.example.bolna.chats.data.dto.RoomDto
import com.example.bolna.chats.model.ChatRoom
import com.example.bolna.chats.model.RoomType
import java.time.Instant


//Extension function for RoomDto to map DTO into UI model
fun RoomDto.toModel(): ChatRoom {
    val created = createdAt?.let { Instant.parse(it) } ?: Instant.EPOCH
    return ChatRoom(
        id = roomId,
        name = name ?: "Unnamed room",
        type = when (roomType) {
            "DM" -> RoomType.DM
            "GROUP" -> RoomType.GROUP
            else -> RoomType.UNKNOWN
        },
        createdBy = createdBy,
        createdAt = created,
        avatarUrl = avatarUrl,
        lastMessage = lastMessage,
        // A room with no messages yet falls back to its creation time
        lastActivityAt = lastMessageAt?.let { Instant.parse(it) } ?: created,
        unreadCount = unreadCount ?: 0
    )
}
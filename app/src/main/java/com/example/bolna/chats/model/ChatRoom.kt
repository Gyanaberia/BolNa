package com.example.bolna.chats.model

import java.time.Instant
enum class RoomType { DM, GROUP, UNKNOWN }

data class ChatRoom(
    val id: String,
    val name: String,
    val type: RoomType,
    val createdBy:String?,
    val createdAt:Instant,
    val avatarUrl: String?,
    val lastMessage: String?,
    val lastActivityAt: Instant,   // what we sort by
    val unreadCount: Int,

)
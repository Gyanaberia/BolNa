package com.example.bolna.chats.data.dto

import com.google.gson.annotations.SerializedName

data class RoomsResponseDto(
    val rooms: List<RoomDto>
)

data class RoomDto(
    val roomId: String,
    val name: String?,
    val roomType: String?,
    val createdBy: String?,
    val createdAt: String?,
    val avatarUrl: String?,
    val lastMessage: String?,
    val lastMessageAt: String?,
    @SerializedName("unReadCount") val unreadCount: Int?
)
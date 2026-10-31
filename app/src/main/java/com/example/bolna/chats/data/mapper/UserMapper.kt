package com.example.bolna.chats.data.mapper

import com.example.bolna.chats.data.dto.UserInfoDto
import com.example.bolna.chats.model.StatusType
import com.example.bolna.chats.model.UserInfo
import java.time.Instant

fun UserInfoDto.toModel(): UserInfo{
    return UserInfo(
        userId=userId,
        email=email,
        avatarUrl=avatarUrl?:"",
        status=when(status){
            "OFFLINE"-> StatusType.OFFLINE
            "ONLINE"-> StatusType.ONLINE
            else-> StatusType.UNKNOWN
        },
        lastSeen=lastSeen?.let { Instant.parse(it) } ?: Instant.EPOCH,
        name=name,
        mobileNumber=mobileNumber?:"",
    )
}
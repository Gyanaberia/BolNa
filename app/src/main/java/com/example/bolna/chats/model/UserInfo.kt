package com.example.bolna.chats.model

import java.time.Instant

enum class StatusType{OFFLINE, ONLINE, UNKNOWN}
data class UserInfo (
    val userId: String,
    val email:String,
    val avatarUrl:String,
    val status: StatusType,
    val lastSeen: Instant,
    val name:String,
    val mobileNumber:String,
)

//{
//    "user": {
//    "userId": "3fa85f64-5717-4562-b3fc-2c963f66afa6",
//    "email": "string",
//    "avatarUrl": "string",
//    "status": "string",
//    "lastSeen": "2026-10-31T04:34:05.353Z",
//    "username": "string",
//    "name": "string",
//    "mobileNumber": "string"
//}
//}
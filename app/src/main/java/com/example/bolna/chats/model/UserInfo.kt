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
    val userHandle: String,
    val mobileNumber:String,
)

fun fakeUsers(): List<UserInfo> = listOf(
    UserInfo(
        userId = "u-001", email = "aarav.mehta@example.com", avatarUrl = "",
        status = StatusType.ONLINE, lastSeen = Instant.parse("2026-11-01T17:50:00Z"),
        name = "Aarav Mehta", userHandle = "@aarav", mobileNumber = "+91 90000 00001"
    ),
    UserInfo(
        userId = "u-002", email = "ananya.rao@example.com", avatarUrl = "",
        status = StatusType.OFFLINE, lastSeen = Instant.parse("2026-11-01T15:20:00Z"),
        name = "Ananya Rao", userHandle = "@ananya", mobileNumber = "+91 90000 00002"
    ),
    UserInfo(
        userId = "u-003", email = "dev.patel@example.com", avatarUrl = "",
        status = StatusType.ONLINE, lastSeen = Instant.parse("2026-11-01T12:05:00Z"),
        name = "Dev Patel", userHandle = "@devp", mobileNumber = "+91 90000 00003"
    ),
    UserInfo(
        userId = "u-004", email = "isha.nair@example.com", avatarUrl = "",
        status = StatusType.ONLINE, lastSeen = Instant.parse("2026-11-01T17:58:00Z"),
        name = "Isha Nair", userHandle = "@isha", mobileNumber = "+91 90000 00004"
    ),
    UserInfo(
        userId = "u-005", email = "kabir.singh@example.com", avatarUrl = "",
        status = StatusType.OFFLINE, lastSeen = Instant.parse("2026-10-31T21:30:00Z"),
        name = "Kabir Singh", userHandle = "@kabir", mobileNumber = "+91 90000 00005"
    ),
    UserInfo(
        userId = "u-006", email = "meera.iyer@example.com", avatarUrl = "",
        status = StatusType.OFFLINE, lastSeen = Instant.parse("2026-11-01T09:45:00Z"),
        name = "Meera Iyer", userHandle = "@meera", mobileNumber = "+91 90000 00006"
    ),
    UserInfo(
        userId = "u-007", email = "neha.das@example.com", avatarUrl = "",
        status = StatusType.ONLINE, lastSeen = Instant.parse("2026-11-01T17:40:00Z"),
        name = "Neha Das", userHandle = "@neha", mobileNumber = "+91 90000 00007"
    ),
    UserInfo(
        userId = "u-008", email = "ravi.kumar@example.com", avatarUrl = "",
        status = StatusType.OFFLINE, lastSeen = Instant.parse("2026-10-29T08:15:00Z"),
        name = "Ravi Kumar", userHandle = "@ravik", mobileNumber = "+91 90000 00008"
    ),
    UserInfo(
        userId = "u-009", email = "sana.khan@example.com", avatarUrl = "",
        status = StatusType.ONLINE, lastSeen = Instant.parse("2026-11-01T14:10:00Z"),
        name = "Sana Khan", userHandle = "@sana", mobileNumber = "+91 90000 00009"
    ),
    UserInfo(
        userId = "u-010", email = "tarun.joshi@example.com", avatarUrl = "",
        status = StatusType.OFFLINE, lastSeen = Instant.parse("2026-10-25T19:00:00Z"),
        name = "Tarun Joshi", userHandle = "@tarun", mobileNumber = "+91 90000 00010"
    ),
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
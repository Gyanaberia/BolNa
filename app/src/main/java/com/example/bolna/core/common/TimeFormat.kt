package com.example.bolna.core.common

import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter

fun formatChatTime(instant: Instant): String {
    val zone = ZoneId.systemDefault()
    val date = instant.atZone(zone).toLocalDate()
    val today = LocalDate.now(zone)
    return when (date) {
        today -> DateTimeFormatter.ofPattern("h:mm a").format(instant.atZone(zone))
        today.minusDays(1) -> "Yesterday"
        else -> DateTimeFormatter.ofPattern("dd/MM/yy").format(instant.atZone(zone))
    }
}
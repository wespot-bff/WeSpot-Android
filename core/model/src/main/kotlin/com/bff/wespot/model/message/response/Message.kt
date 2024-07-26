package com.bff.wespot.model.message.response

import java.time.LocalDateTime

data class Message(
    val id: Int,
    val senderName: String,
    val content: String,
    val receivedAt: LocalDateTime,
    val isRead: Boolean,
    val isBlocked: Boolean,
    val readAt: LocalDateTime?,
)

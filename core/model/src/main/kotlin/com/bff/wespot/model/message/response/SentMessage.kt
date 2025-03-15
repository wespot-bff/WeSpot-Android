package com.bff.wespot.model.message.response

import com.bff.wespot.model.user.response.User
import java.time.LocalDateTime

data class SentMessage(
    val id: Int = -1,
    val senderName: String = "",
    val receiver: User = User(),
    val content: String = "",
    val sender: User = User(),
    val receivedAt: LocalDateTime? = null,
    val isRead: Boolean = false,
    val readAt: LocalDateTime? = null,
    val isReported: Boolean = false,
    val isBlocked: Boolean = false,
    val isAnonymous: Boolean = false,
)

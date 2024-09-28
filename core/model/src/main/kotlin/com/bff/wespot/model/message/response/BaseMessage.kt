package com.bff.wespot.model.message.response

import com.bff.wespot.model.user.response.User
import java.time.LocalDateTime

abstract class BaseMessage(
    open val id: Int,
    open val senderName: String,
    open val receiver: User,
    open val content: String,
    open val receivedAt: LocalDateTime?,
    open val isRead: Boolean,
    open val readAt: LocalDateTime?,
)

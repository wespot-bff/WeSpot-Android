package com.bff.wespot.model.message.response

import com.bff.wespot.model.user.response.User
import java.time.LocalDateTime

data class ReceivedMessage(
    val id: Int,
    val senderName: String,
    val receiver: User,
    val content: String,
    val receivedAt: LocalDateTime?,
    val isRead: Boolean,
    val readAt: LocalDateTime?,
) {
    constructor() : this(-1, "", User(), "", LocalDateTime.MAX, false, null)
}

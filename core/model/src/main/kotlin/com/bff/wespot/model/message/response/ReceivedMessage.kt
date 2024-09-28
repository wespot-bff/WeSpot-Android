package com.bff.wespot.model.message.response

import com.bff.wespot.model.user.response.User
import java.time.LocalDateTime

data class ReceivedMessage(
    override val id: Int,
    override val senderName: String,
    override val receiver: User,
    override val content: String,
    override val receivedAt: LocalDateTime?,
    override val isRead: Boolean,
    override val readAt: LocalDateTime?,
) : BaseMessage(id, senderName, receiver, content, receivedAt, isRead, readAt) {
    constructor() : this(-1, "", User(), "", LocalDateTime.MAX, false, null)
}

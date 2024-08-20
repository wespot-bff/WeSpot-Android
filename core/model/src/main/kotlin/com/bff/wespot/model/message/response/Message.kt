package com.bff.wespot.model.message.response

import com.bff.wespot.model.user.response.User
import java.time.LocalDateTime

data class Message(
    val id: Int,
    val senderName: String,
    val receiver: User,
    val content: String,
    val receivedAt: LocalDateTime?,
    val isRead: Boolean,
    val isReported: Boolean,
    val isBlocked: Boolean,
    val isAnonymous: Boolean,
    val readAt: LocalDateTime?,
) {
    constructor() : this(-1, "", User(), "", LocalDateTime.MAX, false, false, false, false, null)

    fun toSentMessage(): SentMessage = SentMessage(
        id = id,
        senderName = senderName,
        receiver = receiver,
        content = content,
        receivedAt = receivedAt,
        isRead = isRead,
        isReported = isReported,
        isBlocked = isBlocked,
        isAnonymous = isAnonymous,
        readAt = readAt,
    )

    fun toReceivedMessage(): ReceivedMessage = ReceivedMessage(
        id = id,
        senderName = senderName,
        receiver = receiver,
        content = content,
        receivedAt = receivedAt,
        isRead = isRead,
        readAt = readAt,
    )
}

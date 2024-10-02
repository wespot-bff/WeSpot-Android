package com.bff.wespot.data.remote.model.message.response

import com.bff.wespot.data.remote.extensions.toISOLocalDateTime
import com.bff.wespot.data.remote.model.user.response.UserDto
import com.bff.wespot.model.message.response.Message
import com.bff.wespot.model.message.response.ReceivedMessage
import kotlinx.serialization.Serializable

@Serializable
data class MessageDto(
    val id: Int,
    val senderName: String,
    val receiver: UserDto,
    val content: String,
    val receivedAt: String? = "",
    val isRead: Boolean,
    val isReported: Boolean,
    val isBlocked: Boolean,
    val isAnonymous: Boolean,
    val readAt: String? = "",
) {
    fun toMessage(): Message = Message(
        id = id,
        senderName = senderName,
        receiver = receiver.toUser(),
        content = content,
        receivedAt = receivedAt?.toISOLocalDateTime(),
        isRead = isRead,
        isReported = isReported,
        isBlocked = isBlocked,
        isAnonymous = isAnonymous,
        readAt = readAt?.toISOLocalDateTime(),
    )

    fun toReceivedMessage(): ReceivedMessage = ReceivedMessage(
        id = id,
        senderName = senderName,
        receiver = receiver.toUser(),
        content = content,
        receivedAt = receivedAt?.toISOLocalDateTime(),
        isRead = isRead,
        readAt = readAt?.toISOLocalDateTime(),
    )
}

package com.bff.wespot.data.remote.model.message.response

import com.bff.wespot.model.message.response.Message
import com.bff.wespot.data.remote.extensions.toISOLocalDateTime
import kotlinx.serialization.Serializable

@Serializable
data class MessageDto (
    val id: Int,
    val senderName: String,
    val content: String,
    val receivedAt: String,
    val isRead: Boolean,
    val isBlocked: Boolean,
    val readAt: String?,
) {
    fun toMessage(): Message = Message(
        id = id,
        senderName = senderName,
        content = content,
        receivedAt = receivedAt.toISOLocalDateTime(),
        isRead = isRead,
        isBlocked = isBlocked,
        readAt = readAt?.toISOLocalDateTime(),
    )
}
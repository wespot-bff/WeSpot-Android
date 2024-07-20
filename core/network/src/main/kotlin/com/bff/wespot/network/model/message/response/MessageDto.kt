package com.bff.wespot.network.model.message.response

import com.bff.wespot.model.message.response.Message
import com.bff.wespot.network.extensions.toISOLocalDateTime
import kotlinx.serialization.Serializable

@Serializable
data class MessageDto (
    val id: Int,
    val senderName: String,
    val content: String,
    val receivedAt: String,
    val read: Boolean,
    val readAt: String?,
) {
    fun toMessage(): Message = Message(
        id = id,
        senderName = senderName,
        content = content,
        receivedAt = receivedAt.toISOLocalDateTime(),
        read = read,
        readAt = readAt?.toISOLocalDateTime(),
    )
}
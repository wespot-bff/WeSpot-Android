package com.bff.wespot.data.remote.model.message.response

import com.bff.wespot.model.message.response.MessageStatus
import kotlinx.serialization.Serializable

@Serializable
data class MessageStatusDto (
    val canSend: Boolean,
    val remainingMessages: Int,
) {
    fun toMessageStatus() = MessageStatus(
        canSend = canSend,
        remainingMessages = remainingMessages,
    )
}
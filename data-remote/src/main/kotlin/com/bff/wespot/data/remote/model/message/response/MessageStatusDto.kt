package com.bff.wespot.data.remote.model.message.response

import com.bff.wespot.model.message.response.MessageStatus
import kotlinx.serialization.Serializable

@Serializable
data class MessageStatusDto (
    val isSendAllowed: Boolean,
    val remainingMessages: Int,
) {
    fun toMessageStatus() = MessageStatus(
        isSendAllowed = isSendAllowed,
        remainingMessages = remainingMessages,
    )
}

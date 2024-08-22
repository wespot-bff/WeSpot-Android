package com.bff.wespot.data.remote.model.message.response

import com.bff.wespot.model.message.response.MessageStatus
import kotlinx.serialization.Serializable

@Serializable
data class MessageStatusDto (
    val isSendAllowed: Boolean,
    val countRemainingMessages: Int,
    val countUnReadMessages: Int,
) {
    fun toMessageStatus() = MessageStatus(
        isSendAllowed = isSendAllowed,
        countRemainingMessages = countRemainingMessages,
        countUnReadMessages = countUnReadMessages,
    )
}

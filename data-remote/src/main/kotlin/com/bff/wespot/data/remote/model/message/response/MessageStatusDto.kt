package com.bff.wespot.data.remote.model.message.response

import com.bff.wespot.model.message.response.MessageStatus
import kotlinx.serialization.Serializable

@Serializable
data class MessageStatusDto (
    val isSendAllowed: Boolean,
    val isReceivedAllowed: Boolean,
    val countRemainingMessages: Int,
    val countUnReadMessages: Int,
    val countUnReplayMessages: Int,
) {
    fun toMessageStatus() = MessageStatus(
        isSendAllowed = isSendAllowed,
        isReceivedAllowed = isReceivedAllowed,
        countRemainingMessages = countRemainingMessages,
        countUnReadMessages = countUnReadMessages,
        countUnReplayMessages = countUnReplayMessages,
    )
}

package com.bff.wespot.data.remote.model.message.response

import com.bff.wespot.model.message.response.ReceivedMessageList
import com.bff.wespot.model.message.response.SentMessageList
import kotlinx.serialization.Serializable

@Serializable
data class MessageListDto (
    val messages: List<MessageDto>,
    val lastCursorId: Int,
    val hasNext: Boolean,
) {
    fun toReceivedMessageList(): ReceivedMessageList = ReceivedMessageList(
        data = messages.map { it.toReceivedMessage() },
        lastCursorId = lastCursorId,
        hasNext = hasNext,
    )

    fun toSentMessageList(): SentMessageList = SentMessageList(
        data = messages.map { it.toSentMessage() },
        lastCursorId = lastCursorId,
        hasNext = hasNext,
    )
}

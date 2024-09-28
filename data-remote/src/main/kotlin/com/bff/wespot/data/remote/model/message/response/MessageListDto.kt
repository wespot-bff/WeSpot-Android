package com.bff.wespot.data.remote.model.message.response

import com.bff.wespot.model.message.response.MessageList
import com.bff.wespot.model.message.response.ReceivedMessageList
import kotlinx.serialization.Serializable

@Serializable
data class MessageListDto (
    val messages: List<MessageDto>,
    val lastCursorId: Int,
    val hasNext: Boolean,
) {
    fun toMessageList(): MessageList = MessageList(
        data = messages.map { it.toMessage() },
        lastCursorId = lastCursorId,
        hasNext = hasNext,
    )

    fun toReceivedMessageList(): ReceivedMessageList = ReceivedMessageList(
        data = messages.map { it.toReceivedMessage() },
        lastCursorId = lastCursorId,
        hasNext = hasNext,
    )
}

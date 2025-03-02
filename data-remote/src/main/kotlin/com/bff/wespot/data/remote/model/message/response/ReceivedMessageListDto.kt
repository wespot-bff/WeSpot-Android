package com.bff.wespot.data.remote.model.message.response

import com.bff.wespot.data.remote.extensions.toISOLocalDateTime
import com.bff.wespot.data.remote.model.user.response.UserDto
import com.bff.wespot.model.message.response.ReceivedMessage
import com.bff.wespot.model.message.response.ReceivedMessageList
import kotlinx.serialization.Serializable

@Serializable
data class ReceivedMessageListDto(
    val messages: List<ReceivedMessageDto>,
    val lastCursorId: Int,
    val hasNext: Boolean,
) {
    fun toReceivedMessageList(): ReceivedMessageList = ReceivedMessageList(
        data = messages.map { it.toReceivedMessage() },
        lastCursorId = lastCursorId,
        hasNext = hasNext,
    )

    @Serializable
    data class ReceivedMessageDto(
        val id: Int = -1,
        val senderName: String,
        val receiver: UserDto,
        val content: String,
        val sender: UserDto = UserDto(),
        val receivedAt: String = "",
        val isRead: Boolean = false,
        val isAnonymous: Boolean = false,
        val readAt: String = "",
    ) {
        fun toReceivedMessage(): ReceivedMessage = ReceivedMessage(
            id = id,
            senderName = senderName,
            receiver = receiver.toUser(),
            content = content,
            sender = sender.toUser(),
            receivedAt = receivedAt.toISOLocalDateTime(),
            isRead = isRead,
            isAnonymous = isAnonymous,
            readAt = readAt.toISOLocalDateTime(),
        )
    }
}

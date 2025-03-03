package com.bff.wespot.data.remote.model.message.response

import com.bff.wespot.data.remote.extensions.toISOLocalDateTime
import com.bff.wespot.data.remote.model.user.response.UserDto
import com.bff.wespot.model.message.response.SentMessage
import com.bff.wespot.model.message.response.SentMessageList
import kotlinx.serialization.Serializable

@Serializable
data class SentMessageListDto (
    val messages: List<SentMessageDto>,
    val lastCursorId: Int,
    val hasNext: Boolean,
) {
    fun toSentMessageList(): SentMessageList = SentMessageList(
        data = messages.map { it.toSentMessage() },
        lastCursorId = lastCursorId,
        hasNext = hasNext,
    )

    @Serializable
    data class SentMessageDto(
        val id: Int,
        val senderName: String,
        val sender: UserDto = UserDto(),
        val receiver: UserDto,
        val content: String,
        val receivedAt: String = "",
        val isRead: Boolean,
        val isReported: Boolean,
        val isBlocked: Boolean,
        val isAnonymous: Boolean,
        val readAt: String = "",
    ) {
        fun toSentMessage(): SentMessage = SentMessage(
            id = id,
            senderName = senderName,
            sender = sender.toUser(),
            receiver = receiver.toUser(),
            content = content,
            receivedAt = receivedAt.toISOLocalDateTime(),
            isRead = isRead,
            isReported = isReported,
            isBlocked = isBlocked,
            isAnonymous = isAnonymous,
            readAt = readAt.toISOLocalDateTime(),
        )
    }
}

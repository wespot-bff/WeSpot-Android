package com.bff.wespot.data.remote.model.message.response

import com.bff.wespot.data.remote.extensions.toISOLocalDateTime
import com.bff.wespot.data.remote.model.user.response.UserDto
import com.bff.wespot.model.message.response.BlockedMessage
import com.bff.wespot.model.message.response.BlockedMessageList
import kotlinx.serialization.Serializable

@Serializable
data class BlockedMessageListDto(
    val messages: List<BlockedMessageDto>,
    val lastCursorId: Int,
    val hasNext: Boolean,
) {
    fun toBlockedMessageList(): BlockedMessageList = BlockedMessageList(
        data = messages.map { it.toBlockedMessage() },
        lastCursorId = lastCursorId,
        hasNext = hasNext,
    )
}

@Serializable
data class BlockedMessageDto(
    val id: Int,
    val senderName: String,
    val senderProfile: SenderDto,
    val receiver: UserDto,
    val content: String,
    val receivedAt: String?,
    val isRead: Boolean,
    val isReported: Boolean,
    val isBlocked: Boolean,
) {
    fun toBlockedMessage(): BlockedMessage = BlockedMessage(
        id = id,
        senderName = senderName,
        senderProfile = senderProfile.toSender(),
        receiver = receiver.toUser(),
        content = content,
        receivedAt = receivedAt?.toISOLocalDateTime(),
        isRead = isRead,
        isReported = isReported,
        isBlocked = isBlocked,
    )
}

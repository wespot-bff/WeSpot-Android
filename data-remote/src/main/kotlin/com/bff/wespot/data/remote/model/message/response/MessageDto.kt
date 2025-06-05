package com.bff.wespot.data.remote.model.message.response

import com.bff.wespot.data.remote.extensions.toISOLocalDateTime
import com.bff.wespot.model.message.response.Message
import kotlinx.serialization.Serializable

@Serializable
data class MessageDto(
    val id: Int,
    val senderProfile: MessageProfileDto,
    val receiverProfile: MessageProfileDto,
    val isExistsUnreadMessage: Boolean,
    val latestChatTime: String,
    val isMeMessageRoomOwner: Boolean,
    val isBookmarked: Boolean,
    val isBlocked: Boolean,
    val isEver: Boolean,
) {
    fun toMessage() = Message(
        id = id,
        senderProfile = senderProfile.toMessageProfile(),
        receiverProfile = receiverProfile.toMessageProfile(),
        isExistsUnreadMessage = isExistsUnreadMessage,
        isMeMessageRoomOwner = isMeMessageRoomOwner,
        latestChatTime = latestChatTime.toISOLocalDateTime(),
        isBookmarked = isBookmarked,
        isBlocked = isBlocked,
        isEver = isEver,
    )
}

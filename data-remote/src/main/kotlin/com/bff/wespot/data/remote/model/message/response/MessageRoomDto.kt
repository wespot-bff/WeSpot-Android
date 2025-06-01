package com.bff.wespot.data.remote.model.message.response

import com.bff.wespot.model.message.response.MessageRoom
import kotlinx.serialization.Serializable

@Serializable
data class MessageRoomDto(
    val messageRoomId: Int,
    val name: String,
    val thumbnail: String,
    val isBookmarked: Boolean,
    val isReceiverAnonymous: Boolean,
    val messageDetails: List<MessageDetailDto>
) {
    fun toDomain(): MessageRoom = MessageRoom(
        messageRoomId = messageRoomId,
        name = name,
        thumbnail = thumbnail,
        isBookmarked = isBookmarked,
        messageDetails = messageDetails.map { it.toDomain() },
    )
}

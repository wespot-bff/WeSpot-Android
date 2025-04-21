package com.bff.wespot.data.remote.model.message.response

import com.bff.wespot.data.remote.extensions.toISOLocalDateTime
import com.bff.wespot.model.message.response.MessageRoom
import kotlinx.serialization.Serializable

@Serializable
data class MessageRoomDto(
    val messageRoomId: Long,
    val name: String,
    val thumbnail: String,
    val isBookmarked: Boolean,
    val messageDetails: List<MessageDetailDto>
) {
    fun toDomain(): MessageRoom = MessageRoom(
        messageRoomId = messageRoomId,
        name = name,
        thumbnail = thumbnail,
        isBookmarked = isBookmarked,
        messageDetails = messageDetails.map { it.toDomain() },
    )

    @Serializable
    data class MessageDetailDto(
        val id: Long,
        val createdAt: String,
        val content: String,
        val isReceived: Boolean,
        val isSend: Boolean
    ) {
        fun toDomain(): MessageRoom.MessageDetail = MessageRoom.MessageDetail(
            id = id,
            createdAt = createdAt.toISOLocalDateTime(),
            content = content,
            isReceived = isReceived,
            isSend = isSend,
        )
    }
}

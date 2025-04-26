package com.bff.wespot.model.message.response

import java.time.LocalDateTime

data class MessageRoom(
    val messageRoomId: Int = -1,
    val name: String = "",
    val thumbnail: String = "",
    val isBookmarked: Boolean = false,
    val messageDetails: List<MessageDetail> = listOf(),
) {
    data class MessageDetail(
        val id: Int = -1,
        val createdAt: LocalDateTime? = LocalDateTime.MIN,
        val content: String = "",
        val isReceived: Boolean = true,
        val isSend: Boolean = false,
    )

    fun isLastMessage(message: MessageDetail): Boolean {
        return messageDetails.lastOrNull()?.id == message.id
    }
}

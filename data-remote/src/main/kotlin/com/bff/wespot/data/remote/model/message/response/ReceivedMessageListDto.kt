package com.bff.wespot.data.remote.model.message.response

import com.bff.wespot.data.remote.model.user.response.UserDto
import kotlinx.serialization.Serializable

@Serializable
data class ReceivedMessageListDto(
    val messages: List<ReceivedMessageDto>,
    val lastCursorId: Int,
    val hasNext: Boolean,
) {
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
    )
}

package com.bff.wespot.data.remote.model.message.response

import com.bff.wespot.data.remote.extensions.toISOLocalDateTime
import com.bff.wespot.model.message.response.MessageDetail
import kotlinx.serialization.Serializable

@Serializable
data class MessageDetailDto(
    val id: Int,
    val createdAt: String,
    val content: String,
    val isReceived: Boolean,
    val isSend: Boolean,
    val isRead: Boolean = false,
    val isAbleToAnswer: Boolean = false,
) {
    fun toDomain(): MessageDetail = MessageDetail(
        id = id,
        createdAt = createdAt.toISOLocalDateTime(),
        content = content,
        isReceived = isReceived,
        isSend = isSend,
        isRead = isRead,
        isAbleToAnswer = isAbleToAnswer,
    )
}

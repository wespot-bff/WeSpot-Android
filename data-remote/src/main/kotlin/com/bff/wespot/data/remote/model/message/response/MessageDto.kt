package com.bff.wespot.data.remote.model.message.response

import com.bff.wespot.model.message.response.Message
import com.bff.wespot.data.remote.extensions.toISOLocalDateTime
import kotlinx.serialization.Serializable

@Serializable
data class MessageDto(
    val id: Int,
    val senderName: String,
    val receiverId: Int,
    val receiverName: String,
    val receiverSchoolName: String,
    val receiverGrade : Int,
    val receiverClassNumber : Int,
    val content: String,
    val receivedAt: String?,
    val isRead: Boolean,
    val isReported: Boolean,
    val isBlocked: Boolean,
    val readAt: String?
) {
    fun toMessage(): Message = Message(
        id = id,
        senderName = senderName,
        receiverId = receiverId,
        receiverName = receiverName,
        receiverSchoolName = receiverSchoolName,
        receiverGrade = receiverGrade,
        receiverClassNumber = receiverClassNumber,
        content = content,
        receivedAt = receivedAt?.toISOLocalDateTime(),
        isRead = isRead,
        isReported = isReported,
        isBlocked = isBlocked,
        readAt = readAt?.toISOLocalDateTime(),
    )
}

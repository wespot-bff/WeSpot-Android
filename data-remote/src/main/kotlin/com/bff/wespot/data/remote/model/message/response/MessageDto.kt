package com.bff.wespot.data.remote.model.message.response

import com.bff.wespot.data.remote.extensions.toISOLocalDateTime
import com.bff.wespot.model.message.response.Message
import kotlinx.serialization.Serializable

@Serializable
data class MessageDto(
    val id: Int,
    val thumbnail: String,
    val isExistsUnreadMessage: Boolean,
    val latestChatTime: String,
    val isAnonymous: Boolean,
    val name: String,
    val schoolName: String?,
    val grade: Int?,
    val classNumber: Int?,
    val isBookmarked: Boolean,
    val isReported: Boolean,
    val isBlocked: Boolean,
    val isEver: Boolean
) {
    fun toMessage() = Message(
        id = id,
        thumbnail = thumbnail,
        isExistsUnreadMessage = isExistsUnreadMessage,
        latestChatTime = latestChatTime.toISOLocalDateTime(),
        isAnonymous = isAnonymous,
        name = name,
        schoolName = schoolName,
        grade = grade,
        classNumber = classNumber,
        isBookmarked = isBookmarked,
        isReported = isReported,
        isBlocked = isBlocked,
        isEver = isEver
    )
}

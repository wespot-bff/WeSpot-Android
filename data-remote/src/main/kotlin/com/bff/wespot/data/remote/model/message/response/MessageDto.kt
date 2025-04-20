package com.bff.wespot.data.remote.model.message.response

import kotlinx.serialization.Serializable

@Serializable
data class MessageDto(
    val id: Int,
    val thumbnail: String,
    val isExistsUnreadMessage: Boolean,
    val latestChatTime: String,
    val isAnonymous: Boolean,
    val isMeMessageRoomOwner: Boolean,
    val name: String,
    val schoolName: String?,
    val grade: Int?,
    val classNumber: Int?,
    val isBookmarked: Boolean,
    val isReported: Boolean,
    val isBlocked: Boolean,
    val isEver: Boolean,
)

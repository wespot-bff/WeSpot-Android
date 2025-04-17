package com.bff.wespot.model.message.response

import java.time.LocalDateTime

/**
 * [schoolName], [grade], [classNumber] 익명인 경우 null로 내려온다.
 */
data class Message(
    val id: Int,
    val thumbnail: String,
    val isExistsUnreadMessage: Boolean,
    val latestChatTime: LocalDateTime?,
    val isAnonymous: Boolean,
    val name: String,
    val schoolName: String?,
    val grade: Int?,
    val classNumber: Int?,
    val isBookmarked: Boolean,
    val isReported: Boolean,
    val isBlocked: Boolean,
    val isEver: Boolean,
)

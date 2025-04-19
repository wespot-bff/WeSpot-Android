package com.bff.wespot.model.message.response

import java.time.LocalDateTime

/**
 * [schoolName], [grade], [classNumber] 익명인 경우 null로 내려온다.
 */
data class Message(
    val id: Int = -1,
    val thumbnail: String = "",
    val isExistsUnreadMessage: Boolean = false,
    val latestChatTime: LocalDateTime? = LocalDateTime.MIN,
    val isAnonymous: Boolean = false,
    val name: String = "",
    val schoolName: String? = "",
    val grade: Int? = -1,
    val classNumber: Int? = -1,
    val isBookmarked: Boolean = false,
    val isReported: Boolean = false,
    val isBlocked: Boolean = false,
    val isEver: Boolean = false,
)

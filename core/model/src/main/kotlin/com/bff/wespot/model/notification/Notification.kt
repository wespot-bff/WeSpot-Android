package com.bff.wespot.model.notification

import java.time.LocalDate
import java.time.LocalDateTime

data class Notification(
    val id: Int,
    val type: NotificationType,
    val date: LocalDate,
    val targetId: Int,
    val content: String,
    val isNew: Boolean,
    val isEnable: Boolean,
    val createdAt: LocalDateTime,
) {
    fun isTodayVoteResult(): Boolean =
        type == NotificationType.VOTE_RESULT && LocalDate.now().equals(date)
}

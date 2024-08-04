package com.bff.wespot.model.notification

import java.time.LocalDateTime

data class Notification(
    val id: Int,
    val type: NotificationType,
    val targetId: Int,
    val content: String,
    val isNew: Boolean,
    val isEnable: Boolean,
    val createdAt: LocalDateTime,
)

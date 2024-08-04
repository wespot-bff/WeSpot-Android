package com.bff.wespot.data.remote.model.notification

import com.bff.wespot.data.remote.extensions.toISOLocalDateTime
import com.bff.wespot.data.remote.extensions.toLocalDateFromDashPattern
import com.bff.wespot.model.notification.Notification
import kotlinx.serialization.Serializable
import java.time.LocalDateTime

@Serializable
data class NotificationListDto(
    val notifications: List<NotificationDto>
)

@Serializable
data class NotificationDto (
    val id: Int,
    val type: NotificationTypeDto,
    val date: String,
    val targetId: Int,
    val content: String,
    val isNew: Boolean,
    val isEnable: Boolean,
    val createdAt: String,
) {
    fun toNotification(): Notification = Notification(
        id = id,
        type = type.toNotificationType(),
        date = date.toLocalDateFromDashPattern(),
        targetId = targetId,
        content = content,
        isNew = isNew,
        isEnable = isEnable,
        createdAt = createdAt.toISOLocalDateTime() ?: LocalDateTime.MIN,
    )
}

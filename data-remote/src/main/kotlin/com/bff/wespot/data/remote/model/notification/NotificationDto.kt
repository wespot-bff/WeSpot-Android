package com.bff.wespot.data.remote.model.notification

import com.bff.wespot.data.remote.extensions.toISOLocalDateTime
import com.bff.wespot.data.remote.extensions.toLocalDateFromDashPattern
import com.bff.wespot.model.notification.Notification
import com.bff.wespot.model.notification.NotificationType
import kotlinx.serialization.Serializable
import java.time.LocalDateTime

@Serializable
data class NotificationListDto(
    val notifications: List<NotificationDto>,
    val lastCursorId: Int,
    val hasNext: Boolean,
)

@Serializable
data class NotificationDto (
    val id: Int,
    val type: String,
    val date: String,
    val targetId: Int,
    val content: String,
    val isNew: Boolean,
    val isEnable: Boolean,
    val createdAt: String,
) {
    fun toNotification(): Notification = Notification(
        id = id,
        type = type.convertToNotificationType(),
        date = date.toLocalDateFromDashPattern(),
        targetId = targetId,
        content = content,
        isNew = isNew,
        isEnable = isEnable,
        createdAt = createdAt.toISOLocalDateTime() ?: LocalDateTime.MIN,
    )

    private fun String.convertToNotificationType(): NotificationType {
        return when (this) {
            NotificationType.MESSAGE.name -> NotificationType.MESSAGE
            NotificationType.MESSAGE_SENT.name -> NotificationType.MESSAGE_SENT
            NotificationType.MESSAGE_RECEIVED.name -> NotificationType.MESSAGE_RECEIVED
            NotificationType.VOTE.name -> NotificationType.VOTE
            NotificationType.VOTE_RESULT.name -> NotificationType.VOTE_RESULT
            NotificationType.VOTE_RECEIVED.name -> NotificationType.VOTE_RECEIVED
            else -> NotificationType.IDLE
        }
    }
}

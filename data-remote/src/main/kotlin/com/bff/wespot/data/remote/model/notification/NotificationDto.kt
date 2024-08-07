package com.bff.wespot.data.remote.model.notification

import com.bff.wespot.data.remote.extensions.toISOLocalDateTime
import com.bff.wespot.data.remote.extensions.toLocalDateFromDashPattern
import com.bff.wespot.model.notification.Notification
import com.bff.wespot.model.notification.NotificationType
import kotlinx.serialization.Serializable
import java.time.LocalDateTime

@Serializable
data class NotificationListDto(
    val notifications: List<NotificationDto>
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
            MESSAGE.name -> NotificationType.MESSAGE
            MESSAGE_SENT.name -> NotificationType.MESSAGE_SENT
            MESSAGE_RECEIVED.name -> NotificationType.MESSAGE_RECEIVED
            VOTE.name -> NotificationType.VOTE
            VOTE_RESULT.name -> NotificationType.VOTE_RESULT
            VOTE_RECEIVED.name -> NotificationType.VOTE_RECEIVED
            else -> NotificationType.IDLE
        }
    }
}

package com.bff.wespot.data.remote.model.notification

import com.bff.wespot.model.notification.NotificationType
import kotlinx.serialization.Serializable

@Serializable
enum class NotificationTypeDto {
    MESSAGE,
    MESSAGE_SENT,
    MESSAGE_RECEIVED,
    VOTE,
    VOTE_RESULT,
    VOTE_RECEIVED,
    ;

    fun toNotificationType() = when (this) {
        MESSAGE -> NotificationType.MESSAGE
        MESSAGE_SENT -> NotificationType.MESSAGE_SENT
        MESSAGE_RECEIVED -> NotificationType.MESSAGE_RECEIVED
        VOTE -> NotificationType.VOTE
        VOTE_RESULT -> NotificationType.VOTE_RESULT
        VOTE_RECEIVED -> NotificationType.VOTE_RECEIVED
    }
}

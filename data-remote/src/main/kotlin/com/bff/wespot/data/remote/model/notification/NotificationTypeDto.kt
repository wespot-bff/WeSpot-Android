package com.bff.wespot.data.remote.model.notification

import com.bff.wespot.model.notification.NotificationType
import kotlinx.serialization.Serializable

@Serializable
enum class NotificationTypeDto {
    VOTE, MESSAGE;

    fun toNotificationType() = when (this) {
        VOTE -> NotificationType.VOTE
        MESSAGE -> NotificationType.MESSAGE
    }
}

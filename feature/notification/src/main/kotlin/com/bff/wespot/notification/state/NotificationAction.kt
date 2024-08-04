package com.bff.wespot.notification.state

import com.bff.wespot.model.notification.Notification

sealed class NotificationAction {
    data object OnNotificationScreenEntered : NotificationAction()
    data class OnNotificationClicked(val notification: Notification) : NotificationAction()
}

package com.bff.wespot.notification.state

import com.bff.wespot.model.notification.Notification

data class NotificationUiState(
    val notificationList: List<Notification> = listOf(),
)

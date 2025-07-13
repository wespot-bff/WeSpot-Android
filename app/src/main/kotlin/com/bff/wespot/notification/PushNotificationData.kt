package com.bff.wespot.notification

import com.bff.wespot.model.notification.NotificationType

data class PushNotificationData(
    val type: NotificationType,
    val deepLink: String,
    val date: String,
)

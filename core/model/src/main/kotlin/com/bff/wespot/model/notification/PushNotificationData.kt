package com.bff.wespot.model.notification

data class PushNotificationData(
    val type: NotificationType,
    val deepLink: String,
    val date: String,
)

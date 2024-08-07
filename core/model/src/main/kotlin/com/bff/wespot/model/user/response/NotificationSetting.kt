package com.bff.wespot.model.user.response

data class NotificationSetting(
    val isEnableVoteNotification: Boolean,
    val isEnableMessageNotification: Boolean,
    val isEnableEventNotification: Boolean,
)

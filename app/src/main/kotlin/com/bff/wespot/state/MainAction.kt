package com.bff.wespot.state

import com.bff.wespot.model.notification.NotificationType

sealed class MainAction {
    data class OnMainScreenEntered(val appVersionName: String) : MainAction()
    data class OnNotificationSet(val isEnableNotification: Boolean) : MainAction()
    data class OnEnteredByPushNotification(
        val type: NotificationType,
        val userId: String,
        val targetId: Int,
        val date: String,
        val appVersion: String,
    ) : MainAction()
}

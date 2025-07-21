package com.bff.wespot.main.state

import com.bff.wespot.main.model.VersionUpdateType
import com.bff.wespot.model.notification.PushNotificationData

sealed interface MainSideEffect {
    data class ShowVersionUpdateDialog(val versionUpdateType: VersionUpdateType) : MainSideEffect
    data class NavigateFromPushNotification(val data: PushNotificationData) : MainSideEffect
}

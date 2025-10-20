package com.bff.wespot.data.remote.model.user.response

import com.bff.wespot.model.user.response.NotificationSetting
import kotlinx.serialization.Serializable

@Serializable
data class NotificationSettingDto (
    val isEnablePostNotification: Boolean,
    val isEnableVoteNotification: Boolean,
    val isEnableMessageNotification: Boolean,
    val isEnableMarketingNotification: Boolean,
) {
    fun toNotificationSetting(): NotificationSetting = NotificationSetting(
        isEnablePostNotification = isEnablePostNotification,
        isEnableVoteNotification = isEnableVoteNotification,
        isEnableMessageNotification = isEnableMessageNotification,
        isEnableMarketingNotification = isEnableMarketingNotification,
    )
}

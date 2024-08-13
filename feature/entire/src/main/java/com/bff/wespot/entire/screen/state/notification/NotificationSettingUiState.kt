package com.bff.wespot.entire.screen.state.notification

import com.bff.wespot.model.user.response.NotificationSetting

data class NotificationSettingUiState(
    val isLoading: Boolean = false,
    val hasScreenBeenEntered: Boolean = false,
    val initialNotificationSetting: NotificationSetting = NotificationSetting(),
    val isEnableVoteNotification: Boolean = false,
    val isEnableMessageNotification: Boolean = false,
    val isEnableMarketingNotification: Boolean = false,
)

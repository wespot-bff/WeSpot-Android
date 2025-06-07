package com.bff.wespot.message.state.setting

import com.bff.wespot.model.user.response.NotificationSetting

data class MessageNotificationSettingUiState(
    val isLoading: Boolean = true,
    val notificationSetting: NotificationSetting = NotificationSetting(),
)

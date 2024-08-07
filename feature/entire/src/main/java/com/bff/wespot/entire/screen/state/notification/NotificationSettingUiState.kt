package com.bff.wespot.entire.screen.state.notification

data class NotificationSettingUiState(
    val isEnableVoteNotification: Boolean = false,
    val isEnableMessageNotification: Boolean = false,
    val isEnableEventNotification: Boolean = false,
)

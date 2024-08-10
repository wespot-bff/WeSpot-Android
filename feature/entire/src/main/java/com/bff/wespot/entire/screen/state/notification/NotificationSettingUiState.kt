package com.bff.wespot.entire.screen.state.notification

data class NotificationSettingUiState(
    val isLoading: Boolean = false,
    val hasScreenBeenEntered: Boolean = false,
    val isEnableVoteNotification: Boolean = false,
    val isEnableMessageNotification: Boolean = false,
    val isEnableMarketingNotification: Boolean = false,
)

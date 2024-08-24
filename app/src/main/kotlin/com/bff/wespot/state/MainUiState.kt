package com.bff.wespot.state

data class MainUiState (
    val isPushNotificationNavigation: Boolean = true,
    val userId: String = "",
    val isEnableVoteNotification: Boolean = false,
    val isEnableMessageNotification: Boolean = false,
)

package com.bff.wespot.message.state.setting

import com.bff.wespot.model.message.response.MessageStatus

data class MessageUsageSettingUiState(
    val isLoading: Boolean = true,
    val initialState: MessageStatus = MessageStatus(),
    val isUsageEnabled: Boolean = false,
)

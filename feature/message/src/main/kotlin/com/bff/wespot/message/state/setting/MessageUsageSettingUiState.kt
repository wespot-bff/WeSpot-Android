package com.bff.wespot.message.state.setting

import com.bff.wespot.model.message.response.MessageStatus

data class MessageUsageSettingUiState(
    val isLoading: Boolean = true,
    val status: MessageStatus = MessageStatus(),
)

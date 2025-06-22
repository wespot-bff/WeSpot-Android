package com.bff.wespot.message.state.setting

import com.bff.wespot.model.message.response.Message

data class BlockedMessageUiState(
    val isLoading: Boolean = false,
    val messageId: Int = -1,
    val messageList: List<Message> = listOf(),
)

package com.bff.wespot.message.state

import com.bff.wespot.model.message.response.MessageStatus
import com.bff.wespot.model.user.response.Profile

data class MessageUiState(
    val messageStatus: MessageStatus = MessageStatus(false, -1, -1),
    val profile: Profile = Profile(),
    val isLoading: Boolean = false,
)

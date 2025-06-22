package com.bff.wespot.message.state.home

import com.bff.wespot.model.message.response.MessageStatus
import com.bff.wespot.model.user.response.Profile

data class MessageHomeUiState(
    val messageStatus: MessageStatus = MessageStatus(),
    val profile: Profile = Profile(),
    val isLoading: Boolean = false,
    val homeTitle: String = "",
)

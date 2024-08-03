package com.bff.wespot.entire.screen.state

import com.bff.wespot.model.message.response.Message
import com.bff.wespot.model.user.response.Profile

data class EntireUiState(
    val profile: Profile = Profile(),
    val revokeReasonList: List<String> = listOf(),
    val revokeConfirmed: Boolean = false,
    val blockList: List<Message> = listOf(),
    val unBlockList: List<Int> = listOf(),
)

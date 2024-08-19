package com.bff.wespot.entire.state

import com.bff.wespot.model.message.response.BlockedMessage
import com.bff.wespot.model.user.response.Profile
import com.bff.wespot.navigation.util.WebLink

data class EntireUiState(
    val profile: Profile = Profile(),
    val revokeReasonList: List<String> = listOf(),
    val revokeConfirmed: Boolean = false,
    val blockedMessageList: List<BlockedMessage> = listOf(),
    val unBlockList: List<Int> = listOf(),
    val unBlockMessageId: Int = -1,
    val isLoading: Boolean = false,
    val webLinkMap: Map<WebLink, String> = mapOf(),
)

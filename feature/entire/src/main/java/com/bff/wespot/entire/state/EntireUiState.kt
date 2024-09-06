package com.bff.wespot.entire.state

import androidx.paging.PagingData
import com.bff.wespot.model.message.response.BlockedMessage
import com.bff.wespot.model.user.response.Profile
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

data class EntireUiState(
    val profile: Profile = Profile(),
    val revokeReasonList: List<String> = listOf(),
    val revokeConfirmed: Boolean = false,
    val blockedMessageList: Flow<PagingData<BlockedMessage>> = flow { },
    val unBlockList: List<Int> = listOf(),
    val unBlockMessageId: Int = -1,
    val isLoading: Boolean = false,
    val webLinkMap: Map<String, String> = mapOf(),
)

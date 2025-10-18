package com.bff.wespot.entire.state

import com.bff.wespot.model.user.response.Profile

data class EntireUiState(
    val profile: Profile = Profile(),
    val revokeReasonList: List<String> = listOf(),
    val inputRevokeReason: String = "",
    val isInputRevokeReasonSelected: Boolean = false,
    val revokeConfirmed: Boolean = false,
    val isLoading: Boolean = false,
    val webLinkMap: Map<String, String> = mapOf(),
) {
    fun getRevokeReasonResult(): List<String> = if (isInputRevokeReasonSelected) {
        revokeReasonList + inputRevokeReason
    } else {
        revokeReasonList
    }
}

package com.bff.wespot.policy.state

data class AnnouncementPolicyUiState(
    val webLinkMap: Map<String, String> = mapOf(),
    val isBottomSheetShown: Boolean = false,
    val isConfirmBottomSheetShown: Boolean = false,
    val isDialogShown: Boolean = false,
    val revokeConfirmed: Boolean = false,
    val isLoading: Boolean = false,
)

package com.bff.wespot.vote.state.profile

data class ProfileUiState(
    val introduction: String = "",
    val iconUrl: String,
    val backgroundColor: String,
    val hasProfanity: Boolean = false,
)

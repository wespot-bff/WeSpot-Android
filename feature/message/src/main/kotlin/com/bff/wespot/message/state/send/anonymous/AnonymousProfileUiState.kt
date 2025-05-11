package com.bff.wespot.message.state.send.anonymous

data class AnonymousProfileUiState(
    val hasNameProfanity: Boolean = false,
    val name: String = "",
    val imageUrl: String = "",
)

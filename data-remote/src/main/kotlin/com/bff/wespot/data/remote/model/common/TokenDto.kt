package com.bff.wespot.data.remote.model.common

import kotlinx.serialization.Serializable

@Serializable
data class TokenDto(
    val refreshToken: String,
)
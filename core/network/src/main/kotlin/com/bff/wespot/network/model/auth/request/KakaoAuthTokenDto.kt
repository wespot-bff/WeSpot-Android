package com.bff.wespot.network.model.auth.request

import kotlinx.serialization.Serializable

@Serializable
data class KakaoAuthTokenDto(
    val socialType: String,
    val identityToken: String,
)
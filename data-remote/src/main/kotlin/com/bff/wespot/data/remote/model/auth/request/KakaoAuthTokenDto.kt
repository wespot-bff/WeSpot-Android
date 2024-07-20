package com.bff.wespot.data.remote.model.auth.request

import kotlinx.serialization.Serializable

@Serializable
data class KakaoAuthTokenDto(
    val socialType: String,
    val identityToken: String,
)
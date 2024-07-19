package com.bff.wespot.network.model.auth.response

import com.bff.wespot.model.auth.response.AuthToken
import kotlinx.serialization.Serializable

@Serializable
data class AuthTokenDto(
    val accessToken: String,
    val refreshToken: String,
    val refreshTokenExpiredAt: String,
) {
    fun toAuthToken() = AuthToken(
        accessToken = accessToken,
        refreshToken = refreshToken,
        refreshTokenExpiredAt = refreshTokenExpiredAt,
    )
}
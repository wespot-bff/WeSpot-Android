package com.bff.wespot.model.auth.response

data class AuthToken(
    val accessToken: String,
    val refreshToken: String,
)

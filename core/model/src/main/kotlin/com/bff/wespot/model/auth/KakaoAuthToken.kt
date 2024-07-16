package com.bff.wespot.model.auth

data class KakaoAuthToken(
    val accessToken: String,
    val idToken: String?,
)

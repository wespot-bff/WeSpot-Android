package com.bff.wespot.model.auth.request

data class SignIn(
    val accessToken: String,
    val versionName: String,
    val socialType: String = "KAKAO",
)

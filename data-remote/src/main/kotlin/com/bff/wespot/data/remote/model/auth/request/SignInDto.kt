package com.bff.wespot.data.remote.model.auth.request

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SignInDto(
    val socialType: String,
    val identityToken: String,
    val fcmToken: String,
    @SerialName("androidVersionName") val versionName: String,
)

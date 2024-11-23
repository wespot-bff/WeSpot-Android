package com.bff.wespot.data.remote.model.auth.request

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SignUpDto(
    val name: String,
    val schoolId: Int,
    val grade: Int,
    val classNumber: Int,
    val gender: String,
    val signUpToken: String,
    @SerialName("androidVersionNameWhenSignUp") val versionName: String,
    val consents: ConsentsDto,
    val introduction: String?,
    val profileUrl: String?,
)

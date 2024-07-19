package com.bff.wespot.network.model.auth.request

import kotlinx.serialization.Serializable

@Serializable
data class SignUpDto(
    val name: String,
    val schoolId: Int,
    val grade: Int,
    val classNumber: Int,
    val gender: String,
    val signUpToken: String,
    val consents: ConsentsDto,
)
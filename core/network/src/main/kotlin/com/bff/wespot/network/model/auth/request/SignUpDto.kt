package com.bff.wespot.network.model.auth.request

import kotlinx.serialization.Serializable

@Serializable
data class SignUpDto(
    val schoolId: Int,
    val grade: Int,
    val group: Int,
    val gender: String,
    val userToken: String,
)
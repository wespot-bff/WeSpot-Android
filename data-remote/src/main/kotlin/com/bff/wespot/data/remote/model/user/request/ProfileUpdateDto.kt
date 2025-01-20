package com.bff.wespot.data.remote.model.user.request

import kotlinx.serialization.Serializable

@Serializable
data class ProfileUpdateDto(
    val introduction: String,
    val url: String?,
)

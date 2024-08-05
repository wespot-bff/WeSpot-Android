package com.bff.wespot.data.remote.model.common

import kotlinx.serialization.Serializable

@Serializable
data class EditProfileDto(
    val introduction: String,
    val profile: UpdateProfileDto,
)

@Serializable
data class UpdateProfileDto(
    val backgroundColor: String,
    val iconUrl: String,
)
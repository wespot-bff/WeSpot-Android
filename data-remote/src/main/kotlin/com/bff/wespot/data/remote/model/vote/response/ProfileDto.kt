package com.bff.wespot.data.remote.model.vote.response

import com.bff.wespot.model.vote.response.Profile
import kotlinx.serialization.Serializable

@Serializable
data class ProfileDto(
    val backgroundColor: String,
    val iconUrl: String
) {
    fun toProfile() =Profile(
            backgroundColor = backgroundColor,
            iconUrl = iconUrl
        )
}
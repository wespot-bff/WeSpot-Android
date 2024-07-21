package com.bff.wespot.data.remote.model.user.response

import com.bff.wespot.model.user.response.ProfileCharacter
import kotlinx.serialization.Serializable

@Serializable
data class ProfileCharacterDto (
    val iconUrl: String,
    val backgroundColor: String,
) {
    fun toProfileCharacter(): ProfileCharacter = ProfileCharacter(
        iconUrl = iconUrl,
        backgroundColor = backgroundColor,
    )
}

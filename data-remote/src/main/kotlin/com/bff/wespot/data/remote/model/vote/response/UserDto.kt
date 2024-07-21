package com.bff.wespot.data.remote.model.vote.response

import com.bff.wespot.model.vote.response.User
import kotlinx.serialization.Serializable

@Serializable
data class UserDto(
    val id: Int,
    val Name: String,
    val profile: ProfileDto
) {
    fun toUser() = User(
            id = id,
            name = Name,
            profile = profile.toProfile()
        )
}

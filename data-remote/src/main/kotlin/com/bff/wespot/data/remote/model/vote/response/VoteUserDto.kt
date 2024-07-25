package com.bff.wespot.data.remote.model.vote.response

import com.bff.wespot.data.remote.model.user.response.ProfileCharacterDto
import com.bff.wespot.model.vote.response.VoteUser
import kotlinx.serialization.Serializable

@Serializable
data class VoteUserDto(
    val id: Int,
    val name: String,
    val profile: ProfileCharacterDto
) {
    fun toVoteUser() = VoteUser(
            id = id,
            name = name,
            profile = profile.toProfileCharacter()
        )
}

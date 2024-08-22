package com.bff.wespot.model.vote.response

import com.bff.wespot.model.user.response.ProfileCharacter

data class VoteUser(
    val id: Int,
    val name: String,
    val introduction: String,
    val profile: ProfileCharacter,
) {
    constructor() : this(0, "", "", ProfileCharacter())
}

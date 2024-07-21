package com.bff.wespot.model.vote.response

data class VoteUser(
    val id: Int,
    val name: String,
    val profile: Profile,
) {
    constructor() : this(0, "", Profile())
}

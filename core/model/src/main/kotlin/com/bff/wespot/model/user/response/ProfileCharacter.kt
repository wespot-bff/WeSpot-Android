package com.bff.wespot.model.user.response

data class ProfileCharacter(
    val iconUrl: String,
    val backgroundColor: String,
) {
    constructor() : this("", "")
}

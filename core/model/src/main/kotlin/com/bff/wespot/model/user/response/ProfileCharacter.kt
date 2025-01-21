package com.bff.wespot.model.user.response

/**
 * params [iconUrl] URL of the profile image.
 * params [backgroundColor] backgroundColor is no longer used.
 */
data class ProfileCharacter(
    val iconUrl: String,
    val backgroundColor: String,
) {
    constructor() : this("", "")
}

package com.bff.wespot.model.user.response

import kotlinx.serialization.Serializable

/**
 * params [iconUrl] URL of the profile image.
 * params [backgroundColor] backgroundColor is no longer used.
 */

@Serializable
data class ProfileCharacter(
    val iconUrl: String,
    val backgroundColor: String,
) {
    constructor() : this("", "")
}

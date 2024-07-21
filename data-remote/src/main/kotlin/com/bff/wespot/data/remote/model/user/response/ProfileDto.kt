package com.bff.wespot.data.remote.model.user.response

import com.bff.wespot.model.user.response.Profile
import kotlinx.serialization.Serializable

@Serializable
data class ProfileDto (
    val id: Int,
    val name: String,
    val schoolName: String,
    val grade: Int,
    val groupNumber: Int,
    val gender: String,
    val introduction: String,
    val profile: ProfileCharacterDto
) {
    fun toProfile(): Profile = Profile(
        id = id,
        name = name,
        schoolName = schoolName,
        grade = grade,
        group = groupNumber,
        gender = gender,
        introduction = introduction,
        profileCharacter = profile.toProfileCharacter()
    )
}

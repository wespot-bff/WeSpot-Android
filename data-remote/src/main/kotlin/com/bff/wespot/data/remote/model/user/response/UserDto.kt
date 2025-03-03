package com.bff.wespot.data.remote.model.user.response

import com.bff.wespot.model.user.response.User
import kotlinx.serialization.Serializable

@Serializable
data class UserDto (
    val id: Int = -1,
    val name: String = "",
    val grade: Int = -1,
    val classNumber: Int = -1,
    val schoolName: String = "",
    val profile: ProfileCharacterDto = ProfileCharacterDto(),
) {
    fun toUser(): User = User(
        id = id,
        name = name,
        grade = grade,
        classNumber = classNumber,
        schoolName = schoolName,
        profileCharacter = profile.toProfileCharacter(),
    )
}

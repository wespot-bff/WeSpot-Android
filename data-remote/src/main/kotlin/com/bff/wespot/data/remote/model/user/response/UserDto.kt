package com.bff.wespot.data.remote.model.user.response

import com.bff.wespot.model.user.response.User
import kotlinx.serialization.Serializable

@Serializable
data class UserDto (
    val id: Int,
    val name: String,
    val grade: Int,
    val classNumber: Int,
    val schoolId: Int,
    val schoolName: String,
    val profile: ProfileCharacterDto,
) {
    fun toUser(): User = User(
        id = id,
        name = name,
        grade = grade,
        classNumber = classNumber,
        schoolName = schoolName,
        schoolId = schoolId,
        profileCharacter = profile.toProfileCharacter(),
    )
}

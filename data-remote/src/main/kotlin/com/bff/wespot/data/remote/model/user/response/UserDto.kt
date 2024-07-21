package com.bff.wespot.data.remote.model.user.response

import com.bff.wespot.model.user.response.User
import kotlinx.serialization.Serializable

@Serializable
data class UserDto (
    val id: Int,
    val name: String,
    val schoolName: String,
    val grade: Int,
    val group: Int,
) {
    fun toUser(): User = User(
        id = id,
        name = name,
        schoolName = schoolName,
        grade = grade,
        group = group,
    )
}

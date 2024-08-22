package com.bff.wespot.data.remote.model.user.response

import com.bff.wespot.model.user.response.UserList
import kotlinx.serialization.Serializable

@Serializable
data class UserListDto (
    val users: List<UserDto>,
    val lastCursorId: Int,
    val hasNext: Boolean,
) {
    fun toUserList() = UserList(
        data = users.map{ it.toUser() },
        lastCursorId = lastCursorId,
        hasNext = hasNext,
    )
}

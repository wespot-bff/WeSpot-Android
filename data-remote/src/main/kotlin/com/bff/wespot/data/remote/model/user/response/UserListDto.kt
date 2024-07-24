package com.bff.wespot.data.remote.model.user.response

import kotlinx.serialization.Serializable

@Serializable
data class UserListDto (
    val users: List<UserDto>,
    val hasNext: Boolean,
)

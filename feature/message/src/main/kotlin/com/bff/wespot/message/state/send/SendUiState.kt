package com.bff.wespot.message.state.send

import com.bff.wespot.model.user.response.User

data class SendUiState(
    val nameInput: String = "",
    val messageInput: String = "",
    val isRandomName: Boolean = false,
    val randomName: String = "",
    val userList: List<User> = listOf(),
    val selectedUser: User = User(),
    val hasProfanity: Boolean = false,
    val sender: String = "",
    val isReservedMessage: Boolean = false,
    val messageId: Int = -1,
    val isLoading: Boolean = false,
)

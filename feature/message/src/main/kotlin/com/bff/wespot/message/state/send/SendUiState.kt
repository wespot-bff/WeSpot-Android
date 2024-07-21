package com.bff.wespot.message.state.send

import com.bff.wespot.model.user.response.Profile
import com.bff.wespot.model.user.response.ProfileCharacter
import com.bff.wespot.model.user.response.User

data class SendUiState(
    val nameInput: String = "",
    val messageInput: String = "",
    val isRandomName: Boolean = false,
    val randomName: String = "",
    val userList: List<User> = listOf(),
    val selectedUser: User = User(-1, "", "", -1, -1),
    val hasProfanity: Boolean = false,
    val profile: Profile = Profile(-1, "", "", -1, -1, "", "", ProfileCharacter("", "")),
)

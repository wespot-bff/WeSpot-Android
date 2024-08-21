package com.bff.wespot.message.state.send

import androidx.paging.PagingData
import com.bff.wespot.model.user.response.User
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

data class SendUiState(
    val nameInput: String = "",
    val messageInput: String = "",
    val isRandomName: Boolean = false,
    val randomName: String = "",
    val userList: Flow<PagingData<User>> = flow { },
    val selectedUser: User = User(),
    val hasProfanity: Boolean = false,
    val sender: String = "",
    val isReservedMessage: Boolean = false,
    val messageId: Int = -1,
    val isLoading: Boolean = false,
    val messageSendFailedDialogContent: String = "",
)

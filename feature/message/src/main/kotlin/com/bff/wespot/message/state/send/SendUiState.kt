package com.bff.wespot.message.state.send

import androidx.paging.PagingData
import com.bff.wespot.common.util.RandomNameGenerator
import com.bff.wespot.model.common.KakaoContent
import com.bff.wespot.model.user.response.Profile
import com.bff.wespot.model.user.response.User
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

data class SendUiState(
    val nameInput: String = "",
    val isInputInitialized: Boolean = false,
    val messageInput: String = "",
    val isRandomName: Boolean = true,
    val randomName: String = RandomNameGenerator().getRandomName(),
    val userList: Flow<PagingData<User>> = flow { },
    val selectedUser: User = User(),
    val hasProfanity: Boolean = false,
    val sender: String = "",
    val isReservedMessage: Boolean = false,
    val messageId: Int = -1,
    val isLoading: Boolean = false,
    val messageSendFailedDialogContent: String = "",
    val kakaoContent: KakaoContent = KakaoContent.EMPTY,
    val profile: Profile = Profile(),
    val isSelectedContext: Boolean = false,
)

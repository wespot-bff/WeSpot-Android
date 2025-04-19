package com.bff.wespot.message.state.send

import androidx.paging.PagingData
import com.bff.wespot.model.common.KakaoContent
import com.bff.wespot.model.message.response.SenderProfile
import com.bff.wespot.model.user.response.Profile
import com.bff.wespot.model.user.response.User
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

/**
 * [isInputInitialized] 검색 후 결과가 없을 때 카카오톡 친구 초대 문구를 노출한다.
 * [isAnonymous] 쪽지 작성시, 실명이 기본 상태이다.
 * [isSelectedContext] 보낸이를 선택한 시점에서는 위치가 변경되지 않고, 이후 시점에서는 상위에 노출한다.
 **/

data class SendUiState(
    val nameInput: String = "",
    val isInputInitialized: Boolean = false,
    val messageInput: String = "",
    val userList: Flow<PagingData<User>> = flow { },
    val selectedUser: User = User(),
    val hasProfanity: Boolean = false,
    val isLoading: Boolean = false,
    val messageSendFailedDialogContent: String = "",
    val kakaoContent: KakaoContent = KakaoContent.EMPTY,
    val isSelectedContext: Boolean = false,
    val profile: Profile = Profile(),
    val isAnonymous: Boolean = false,
    val showProfileSelectBottomSheet: Boolean = false,
    val showProfileImageOptionBottomSheet: Boolean = false,
    val showProfileCreatorModal: Boolean = false,
    val anonymousProfileList: List<SenderProfile> = listOf(),
    val selectedAnonymousProfile: SenderProfile = SenderProfile(),
    val anonymousProfileInput: SenderProfile = SenderProfile(),
    val hasProfileNameProfanity: Boolean = false,
)

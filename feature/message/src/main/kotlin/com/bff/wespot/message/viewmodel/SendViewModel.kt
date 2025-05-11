package com.bff.wespot.message.viewmodel

import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.bff.wespot.analytic.AnalyticsEvent
import com.bff.wespot.analytic.AnalyticsHelper
import com.bff.wespot.common.extension.onNetworkFailure
import com.bff.wespot.domain.repository.BasePagingRepository
import com.bff.wespot.domain.repository.CommonRepository
import com.bff.wespot.domain.repository.message.MessageRepository
import com.bff.wespot.domain.repository.user.ProfileRepository
import com.bff.wespot.domain.usecase.CheckProfanityUseCase
import com.bff.wespot.message.R
import com.bff.wespot.message.common.MESSAGE_MAX_LENGTH
import com.bff.wespot.message.model.AnonymousProfile
import com.bff.wespot.message.state.send.SendAction
import com.bff.wespot.message.state.send.SendSideEffect
import com.bff.wespot.message.state.send.SendUiState
import com.bff.wespot.model.common.KakaoSharingType
import com.bff.wespot.model.common.Paging
import com.bff.wespot.model.message.request.SendMessage
import com.bff.wespot.model.message.response.SenderProfile
import com.bff.wespot.model.user.response.User
import com.bff.wespot.ui.base.BaseViewModel
import com.bff.wespot.ui.model.SideEffect.Companion.toSideEffect
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.postSideEffect
import org.orbitmvi.orbit.syntax.simple.reduce
import org.orbitmvi.orbit.viewmodel.container
import timber.log.Timber
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import javax.inject.Inject

@HiltViewModel
class SendViewModel @Inject constructor(
    private val messageRepository: MessageRepository,
    private val profileRepository: ProfileRepository,
    private val commonRepository: CommonRepository,
    private val userListRepository: BasePagingRepository<User, Paging<User>>,
    private val checkProfanityUseCase: CheckProfanityUseCase,
    private val analyticsHelper: AnalyticsHelper,
) : BaseViewModel(), ContainerHost<SendUiState, SendSideEffect> {
    override val container = container<SendUiState, SendSideEffect>(SendUiState())

    private val nameInput: MutableStateFlow<String> = MutableStateFlow("")
    private val messageInput: MutableStateFlow<String> = MutableStateFlow("")

    fun onAction(action: SendAction) {
        when (action) {
            /** 쪽지 수신자 선택 화면 */
            is SendAction.OnReceiverScreenEntered -> {
                getKakaoContent()
                getProfile()
                observeNameInput()
            }
            is SendAction.OnSearchContentChanged -> handleSearchContentChanged(action.content)
            is SendAction.OnUserSelected -> handleUserSelected(action.user)
            is SendAction.OnSelectDoneButtonClicked -> handleSelectDoneButtonClicked()

            /** 쪽지 내용 작성 화면 */
            is SendAction.OnWriteScreenEntered -> observeMessageInput()
            is SendAction.OnMessageChanged -> handleMessageChanged(action.content)
            is SendAction.OnProfileBottomSheetSelected -> handleProfileBottomSheetSelected(action.senderProfile)
            SendAction.OnProfileAddButtonClicked -> handleProfileAddButtonClicked()
            SendAction.OnProfileBottomSheetClosed -> handleProfileBottomSheetClosed()
            SendAction.OnWriteDoneButtonClicked -> handleWriteDoneButtonClicked()

            /** 쪽지 전송 화면 */
            is SendAction.OnSendButtonClicked -> handleSendMessage()
            SendAction.OnSenderClicked -> handleSenderClicked()

            /** 공통 */
            SendAction.OnMessageScreenEntered -> clearSendUiState()
            SendAction.OnExitDialogCancelButtonClicked -> handleExitDialogCancelButtonClicked()
            SendAction.OnExitDialogExitButtonClicked -> handleExitButtonClicked()
            SendAction.OnTopBarNavigateButtonClicked -> handleTopBarNavigateButtonClicked()
            is SendAction.OnAnonymousProfileSelected -> handleAnonymousProfileSelected(action.profile)
            SendAction.OnAnonymousProfileModalDismiss -> handleAnonymousProfileDismiss()
        }
    }

    private fun handleSearchContentChanged(content: String) = intent {
        reduce {
            nameInput.value = content
            state.copy(
                nameInput = content,
                isInputInitialized = true,
                isSelectedContext = false,
            )
        }
    }

    private fun handleUserSelected(user: User) = intent {
        reduce {
            if (user == state.selectedUser) {
                state.copy(selectedUser = User())
            } else {
                state.copy(
                    selectedUser = user,
                    isSelectedContext = true,
                )
            }
        }
    }

    private fun observeNameInput() {
        viewModelScope.launch {
            nameInput
                .debounce(INPUT_DEBOUNCE_TIME)
                .distinctUntilChanged()
                .collect { name ->
                    if (name.isNotBlank()) {
                        getUserList(name)
                    }
                }
        }
    }

    private fun getUserList(name: String) = intent {
        viewModelScope.launch(coroutineDispatcher) {
            runCatching {
                val result = userListRepository.fetchResultStream(mapOf("name" to name))
                    .cachedIn(viewModelScope)
                reduce { state.copy(userList = result) }
            }
        }
    }

    private fun observeMessageInput() {
        viewModelScope.launch {
            messageInput
                .debounce(INPUT_DEBOUNCE_TIME)
                .distinctUntilChanged()
                .collect { message ->
                    if (message.length <= MESSAGE_MAX_LENGTH) {
                        hasProfanity(message)
                    }
                }
        }
    }

    private fun hasProfanity(content: String) = intent {
        viewModelScope.launch {
            runCatching {
                val hasProfanity = checkProfanityUseCase(content)
                reduce {
                    state.copy(
                        hasProfanity = hasProfanity,
                    )
                }
            }
        }
    }

    private fun handleMessageChanged(content: String) = intent {
        reduce {
            messageInput.value = content
            state.copy(
                messageInput = content,
            )
        }
    }

    private fun getProfile() = intent {
        viewModelScope.launch {
            runCatching {
                profileRepository.getProfile()
            }.onSuccess { profile ->
                reduce {
                    state.copy(profile = profile)
                }
            }
        }
    }

    private fun getKakaoContent() = intent {
        viewModelScope.launch(coroutineDispatcher) {
            commonRepository.getKakaoContent(KakaoSharingType.FIND.name)
                .onSuccess {
                    reduce { state.copy(kakaoContent = it) }
                }
                .onNetworkFailure {
                    postSideEffect(it.toSideEffect())
                }
                .onFailure {
                    Timber.e(it)
                }
        }
    }

    private fun handleWriteDoneButtonClicked() = intent {
        postSideEffect(SendSideEffect.NavigateToMessageSendScreen)
    }

    private fun handleSelectDoneButtonClicked() = intent {
        reduce { state.copy(isLoading = true) }

        viewModelScope.launch {
            messageRepository.getSenderProfileList(state.selectedUser.id)
                .onSuccess {
                    reduce {
                        state.copy(
                            senderProfileList = it,
                            showProfileSelectBottomSheet = true,
                        )
                    }
                }
                .onNetworkFailure {
                    postSideEffect(it.toSideEffect())
                }
                .also {
                    reduce { state.copy(isLoading = false) }
                }
        }
    }

    private fun handleProfileBottomSheetSelected(senderProfile: SenderProfile) = intent {
        reduce {
            state.copy(
                showProfileSelectBottomSheet = false,
                senderProfile = senderProfile,
            )
        }

        if (senderProfile.isNeverTalkBefore()) {
            postSideEffect(SendSideEffect.NavigateToMessageWriteScreen)
            return@intent
        }

        // TODO 답장으로 이동
    }

    private fun handleProfileBottomSheetClosed() = intent {
        reduce {
            state.copy(showProfileSelectBottomSheet = false)
        }
    }

    private fun handleProfileAddButtonClicked() = intent {
        reduce {
            state.copy(
                showProfileSelectBottomSheet = false,
            )
        }
        postSideEffect(SendSideEffect.ShowAnonymousProfileModal)
    }

    private fun handleSenderClicked() = intent {
        postSideEffect(SendSideEffect.ShowAnonymousProfileModal)
    }

    private fun handleSendMessage() = intent {
        reduce { state.copy(isLoading = true) }
        postSideEffect(SendSideEffect.CloseReserveDialog)

        viewModelScope.launch {
            messageRepository.postMessage(
                SendMessage(
                    receiverId = state.selectedUser.id,
                    content = state.messageInput,
                    isAnonymous = state.senderProfile.isAnonymous,
                    anonymousImageUrl = state.senderProfile.image,
                    anonymousProfileName = state.senderProfile.name,
                ),
            ).onSuccess {
                trackMessageSendEvent()
                reduce { state.copy(isLoading = false) }
                postSideEffect(SendSideEffect.ShowToast(R.string.message_reserve_success))
                postSideEffect(SendSideEffect.NavigateToMessage)
            }.onNetworkFailure { exception ->
                postSideEffect(exception.toSideEffect())
            }.onFailure {
                reduce { state.copy(isLoading = false) }
            }
        }
    }

    private fun handleExitDialogCancelButtonClicked() = intent {
        postSideEffect(SendSideEffect.DismissExitDialog)
    }

    private fun handleExitButtonClicked() = intent {
        postSideEffect(SendSideEffect.DismissExitDialog)
        postSideEffect(SendSideEffect.NavigateToMessage)
    }

    private fun handleTopBarNavigateButtonClicked() = intent {
        postSideEffect(SendSideEffect.NavigateUp)
    }

    private fun handleAnonymousProfileSelected(profile: AnonymousProfile) = intent {
        reduce {
            state.copy(
                senderProfile = state.senderProfile.copy(
                    name = profile.name,
                    image = profile.imageUrl,
                ),
            )
        }
        postSideEffect(SendSideEffect.DismissAnonymousProfileModal)
    }

    private fun handleAnonymousProfileDismiss() = intent {
        postSideEffect(SendSideEffect.DismissAnonymousProfileModal)
    }

    private fun clearSendUiState() = intent {
        reduce {
            SendUiState()
        }
        nameInput.value = ""
        messageInput.value = ""
    }

    private fun trackMessageSendEvent() = intent {
        val formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME
        val sendTime = LocalDateTime.now(ZoneId.of("Asia/Seoul")).format(formatter)

        analyticsHelper.logEvent(
            event = AnalyticsEvent(
                type = "message_send",
                extras = listOf(
                    AnalyticsEvent.Param("userId", state.profile.id.toString()),
                    AnalyticsEvent.Param("time", sendTime),
                ),
            ),
        )
    }

    companion object {
        private const val INPUT_DEBOUNCE_TIME = 500L
    }
}

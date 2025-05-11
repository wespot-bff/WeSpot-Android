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
import com.bff.wespot.message.state.send.MessageSendSideEffect
import com.bff.wespot.message.state.send.MessageSendUiState
import com.bff.wespot.message.state.send.receiver.ReceiverAction
import com.bff.wespot.message.state.send.send.SendAction
import com.bff.wespot.message.state.send.writing.WritingAction
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
) : BaseViewModel(), ContainerHost<MessageSendUiState, MessageSendSideEffect> {
    override val container = container<MessageSendUiState, MessageSendSideEffect>(MessageSendUiState())

    private val nameInput: MutableStateFlow<String> = MutableStateFlow("")
    private val messageInput: MutableStateFlow<String> = MutableStateFlow("")

    fun onAction(action: ReceiverAction) {
        when (action) {
            is ReceiverAction.OnReceiverScreenEntered -> {
                getKakaoContent()
                getProfile()
                observeNameInput()
            }
            is ReceiverAction.OnProfileBottomSheetSelected ->
                handleProfileBottomSheetSelected(action.senderProfile)
            ReceiverAction.OnProfileAddButtonClicked -> handleProfileAddButtonClicked()
            ReceiverAction.OnProfileBottomSheetClosed -> handleProfileBottomSheetClosed()
            is ReceiverAction.OnSearchContentChanged -> handleSearchContentChanged(action.content)
            is ReceiverAction.OnUserSelected -> handleUserSelected(action.user)
            is ReceiverAction.OnSelectDoneButtonClicked -> handleSelectDoneButtonClicked()
            ReceiverAction.OnExitDialogCancelButtonClicked -> handleExitDialogCancelButtonClicked()
            ReceiverAction.OnExitDialogExitButtonClicked -> handleExitButtonClicked()
            ReceiverAction.OnTopBarNavigateButtonClicked -> handleTopBarNavigateButtonClicked()
            is ReceiverAction.OnAnonymousProfileSelected -> handleAnonymousProfileSelected(action.profile)
            ReceiverAction.OnAnonymousProfileModalDismiss -> handleAnonymousProfileDismiss()
        }
    }

    fun onAction(action: WritingAction) {
        when (action) {
            is WritingAction.OnWriteScreenEntered -> observeMessageInput()
            is WritingAction.OnMessageChanged -> handleMessageChanged(action.content)
            WritingAction.OnWriteDoneButtonClicked -> handleWriteDoneButtonClicked()
            WritingAction.OnExitDialogCancelButtonClicked -> handleExitDialogCancelButtonClicked()
            WritingAction.OnExitDialogExitButtonClicked -> handleExitButtonClicked()
            WritingAction.OnTopBarNavigateButtonClicked -> handleTopBarNavigateButtonClicked()
        }
    }

    fun onAction(action: SendAction) {
        when (action) {
            is SendAction.OnSendButtonClicked -> handleSendMessage()
            SendAction.OnSenderClicked -> handleSenderClicked()
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
        postSideEffect(MessageSendSideEffect.NavigateToMessageSendScreen)
    }

    private fun handleSelectDoneButtonClicked() = intent {
        reduce { state.copy(isLoading = true) }

        viewModelScope.launch {
            messageRepository.getSenderProfileList(state.selectedUser.id)
                .onSuccess {
                    reduce {
                        state.copy(senderProfileList = it)
                    }
                    postSideEffect(MessageSendSideEffect.ShowProfileSelectBottomSheet)
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
            state.copy(senderProfile = senderProfile)
        }
        postSideEffect(MessageSendSideEffect.DismissProfileSelectBottomSheet)

        if (senderProfile.isNeverTalkBefore()) {
            postSideEffect(MessageSendSideEffect.NavigateToMessageWriteScreen)
            return@intent
        }

        // TODO 답장으로 이동
    }

    private fun handleProfileBottomSheetClosed() = intent {
        postSideEffect(MessageSendSideEffect.DismissProfileSelectBottomSheet)
    }

    private fun handleProfileAddButtonClicked() = intent {
        postSideEffect(MessageSendSideEffect.DismissProfileSelectBottomSheet)
        postSideEffect(MessageSendSideEffect.ShowAnonymousProfileModal)
    }

    private fun handleSenderClicked() = intent {
        postSideEffect(MessageSendSideEffect.ShowAnonymousProfileModal)
    }

    private fun handleSendMessage() = intent {
        reduce { state.copy(isLoading = true) }
        postSideEffect(MessageSendSideEffect.CloseSendConfirmModal)

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
                postSideEffect(MessageSendSideEffect.ShowToast(R.string.message_reserve_success))
                postSideEffect(MessageSendSideEffect.NavigateToMessage)
            }.onNetworkFailure { exception ->
                postSideEffect(exception.toSideEffect())
            }.onFailure {
                reduce { state.copy(isLoading = false) }
            }
        }
    }

    private fun handleExitDialogCancelButtonClicked() = intent {
        postSideEffect(MessageSendSideEffect.DismissExitDialog)
    }

    private fun handleExitButtonClicked() = intent {
        postSideEffect(MessageSendSideEffect.DismissExitDialog)
        postSideEffect(MessageSendSideEffect.NavigateToMessage)
    }

    private fun handleTopBarNavigateButtonClicked() = intent {
        postSideEffect(MessageSendSideEffect.NavigateUp)
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
        postSideEffect(MessageSendSideEffect.DismissAnonymousProfileModal)
    }

    private fun handleAnonymousProfileDismiss() = intent {
        postSideEffect(MessageSendSideEffect.DismissAnonymousProfileModal)
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

    fun clearSendUiState() = intent {
        reduce {
            MessageSendUiState()
        }
        nameInput.value = ""
        messageInput.value = ""
    }

    companion object {
        private const val INPUT_DEBOUNCE_TIME = 500L
    }
}

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
import com.bff.wespot.message.state.send.receiver.ReceiverSideEffect
import com.bff.wespot.message.state.send.send.SendAction
import com.bff.wespot.message.state.send.send.SendSideEffect
import com.bff.wespot.message.state.send.writing.WritingAction
import com.bff.wespot.message.state.send.writing.WritingSideEffect
import com.bff.wespot.model.common.KakaoSharingType
import com.bff.wespot.model.common.Paging
import com.bff.wespot.model.exception.NetworkException
import com.bff.wespot.model.message.request.SendMessage
import com.bff.wespot.model.message.response.SenderProfile
import com.bff.wespot.model.user.response.User
import com.bff.wespot.ui.base.BaseViewModel
import com.bff.wespot.ui.model.SideEffect
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

    private val receiverInput: MutableStateFlow<String> = MutableStateFlow("")
    private val messageInput: MutableStateFlow<String> = MutableStateFlow("")

    /**
     * 쪽지 수신자 선택 화면 Action
     */
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

            ReceiverAction.OnExitDialogCancelButtonClicked -> {
                intent {
                    postSideEffect(ReceiverSideEffect.DismissExitDialog)
                }
            }
            ReceiverAction.OnExitDialogExitButtonClicked -> {
                intent {
                    postSideEffect(ReceiverSideEffect.DismissExitDialog)
                    postSideEffect(ReceiverSideEffect.NavigateToMessage)
                }
            }
            ReceiverAction.OnTopBarNavigateButtonClicked -> {
                intent {
                    postSideEffect(ReceiverSideEffect.NavigateUp)
                }
            }
            is ReceiverAction.OnAnonymousProfileSelected -> {
                handleAnonymousProfileSelected(action.profile)
                intent {
                    postSideEffect(ReceiverSideEffect.DismissAnonymousProfileModal)
                    postSideEffect(ReceiverSideEffect.NavigateToMessageWriteScreen)
                }
            }
            ReceiverAction.OnAnonymousProfileModalDismiss -> {
                intent {
                    postSideEffect(ReceiverSideEffect.DismissAnonymousProfileModal)
                }
            }
        }
    }

    /**
     * 쪽지 내용 작성 화면 Action
     */
    fun onAction(action: WritingAction) {
        when (action) {
            is WritingAction.OnWriteScreenEntered -> observeMessageInput()
            is WritingAction.OnMessageChanged -> handleMessageChanged(action.content)
            WritingAction.OnWriteDoneButtonClicked -> handleWriteDoneButtonClicked()
            WritingAction.OnExitDialogCancelButtonClicked -> {
                intent {
                    postSideEffect(WritingSideEffect.DismissExitDialog)
                }
            }
            WritingAction.OnExitDialogExitButtonClicked -> {
                intent {
                    postSideEffect(WritingSideEffect.DismissExitDialog)
                    postSideEffect(WritingSideEffect.NavigateToMessage)
                }
            }
            WritingAction.OnTopBarNavigateButtonClicked -> {
                intent {
                    postSideEffect(WritingSideEffect.NavigateUp)
                }
            }
        }
    }

    /**
     * 쪽지 전송 화면 Action
     */
    fun onAction(action: SendAction) {
        when (action) {
            is SendAction.OnSendButtonClicked -> handleMessageSend()
            SendAction.OnSenderClicked -> handleSenderClicked()
            SendAction.OnExitDialogCancelButtonClicked -> {
                intent {
                    postSideEffect(SendSideEffect.DismissExitDialog)
                }
            }
            SendAction.OnExitDialogExitButtonClicked -> {
                intent {
                    postSideEffect(SendSideEffect.DismissExitDialog)
                    postSideEffect(SendSideEffect.NavigateToMessage)
                }
            }
            SendAction.OnTopBarNavigateButtonClicked -> {
                intent {
                    postSideEffect(SendSideEffect.NavigateUp)
                }
            }
            is SendAction.OnAnonymousProfileSelected -> {
                handleAnonymousProfileSelected(action.profile)
                intent {
                    postSideEffect(SendSideEffect.DismissAnonymousProfileModal)
                }
            }
            SendAction.OnAnonymousProfileModalDismiss -> {
                intent {
                    postSideEffect(SendSideEffect.DismissAnonymousProfileModal)
                }
            }
        }
    }

    private fun handleSearchContentChanged(content: String) = intent {
        reduce {
            receiverInput.value = content
            state.copy(
                nameInput = content,
                isInputInitialized = true,
                isSelectedContext = false,
            )
        }
    }

    private fun handleUserSelected(user: User) = intent {
        reduce {
            if (user == state.receiver) {
                state.copy(receiver = User())
            } else {
                state.copy(
                    receiver = user,
                    isSelectedContext = true,
                )
            }
        }
    }

    private fun observeNameInput() {
        viewModelScope.launch {
            receiverInput
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
                reduce { state.copy(receiverList = result) }
            }
        }
    }

    private fun observeMessageInput() {
        viewModelScope.launch {
            messageInput
                .debounce(INPUT_DEBOUNCE_TIME)
                .distinctUntilChanged()
                .collect { message ->
                    if (message.isNotEmpty() && message.length <= MESSAGE_MAX_LENGTH) {
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
                    state.copy(hasProfanity = hasProfanity)
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

    private fun handleAnonymousProfileSelected(profile: AnonymousProfile) = intent {
        reduce {
            state.copy(
                senderProfile = state.senderProfile.copy(
                    name = profile.name,
                    image = profile.imageUrl,
                    isAnonymous = true,
                ),
            )
        }
    }

    private fun handleWriteDoneButtonClicked() = intent {
        postSideEffect(WritingSideEffect.NavigateToMessageSendScreen)
    }

    private fun handleSelectDoneButtonClicked() = intent {
        reduce { state.copy(isLoading = true) }

        viewModelScope.launch {
            messageRepository.getSenderProfileList(state.receiver.id)
                .onSuccess {
                    reduce {
                        state.copy(senderProfileList = it)
                    }
                    postSideEffect(ReceiverSideEffect.ShowProfileSelectBottomSheet)
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
        postSideEffect(ReceiverSideEffect.DismissProfileSelectBottomSheet)

        if (senderProfile.isNeverTalkBefore()) {
            postSideEffect(ReceiverSideEffect.NavigateToMessageWriteScreen)
            return@intent
        }

        // TODO 답장으로 이동
    }

    private fun handleProfileBottomSheetClosed() = intent {
        postSideEffect(ReceiverSideEffect.DismissProfileSelectBottomSheet)
    }

    private fun handleProfileAddButtonClicked() = intent {
        reduce {
            state.copy(senderProfile = SenderProfile())
        }

        postSideEffect(ReceiverSideEffect.DismissProfileSelectBottomSheet)
        postSideEffect(ReceiverSideEffect.ShowAnonymousProfileModal)
    }

    private fun handleSenderClicked() = intent {
        postSideEffect(SendSideEffect.ShowAnonymousProfileModal)
    }

    private fun handleMessageSend() = intent {
        reduce { state.copy(isLoading = true) }
        postSideEffect(SendSideEffect.CloseSendConfirmModal)

        viewModelScope.launch {
            val imagePath = state.senderProfile.image
            val imageUrl = if (imagePath.isBlank() || imagePath.startsWith("http")) {
                imagePath
            } else {
                uploadAndGetImageUrl(profilePath = imagePath)
                    .getOrElse { exception ->
                        Timber.d(exception)
                        postSideEffect(SideEffect.toToastEffect())
                        reduce { state.copy(isLoading = false) }
                        return@launch
                    }
            }

            messageRepository.postMessage(
                SendMessage(
                    receiverId = state.receiver.id,
                    content = state.messageInput,
                    isAnonymous = state.senderProfile.isAnonymous,
                    anonymousImageUrl = imageUrl,
                    anonymousProfileName = state.senderProfile.name,
                ),
            ).onSuccess {
                trackMessageSendEvent()
                reduce { state.copy(isLoading = false) }
                postSideEffect(SendSideEffect.ShowToast(R.string.message_send_success))
                postSideEffect(SendSideEffect.NavigateToMessage)
            }.onNetworkFailure { exception ->
                postSideEffect(exception.toSideEffect())
            }.onFailure {
                reduce { state.copy(isLoading = false) }
            }
        }
    }

    private suspend fun uploadAndGetImageUrl(profilePath: String): Result<String> =
        runCatching {
            commonRepository.uploadImage(profilePath)
        }.mapCatching { uploadResult ->
            if (!uploadResult.isSuccess) throw NetworkException()
            uploadResult.getOrThrow()
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

    fun clearUiState() = intent {
        reduce {
            MessageSendUiState()
        }
        receiverInput.value = ""
        messageInput.value = ""
    }

    companion object {
        private const val INPUT_DEBOUNCE_TIME = 500L
    }
}

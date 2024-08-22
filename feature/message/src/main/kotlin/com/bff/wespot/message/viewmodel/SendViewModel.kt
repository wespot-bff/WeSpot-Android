package com.bff.wespot.message.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.bff.wespot.common.extension.onNetworkFailure
import com.bff.wespot.common.util.RandomNameGenerator
import com.bff.wespot.domain.repository.BasePagingRepository
import com.bff.wespot.domain.repository.CommonRepository
import com.bff.wespot.domain.repository.message.MessageRepository
import com.bff.wespot.domain.repository.user.ProfileRepository
import com.bff.wespot.domain.usecase.CheckProfanityUseCase
import com.bff.wespot.message.common.MESSAGE_MAX_LENGTH
import com.bff.wespot.message.state.send.SendAction
import com.bff.wespot.message.state.send.SendSideEffect
import com.bff.wespot.message.state.send.SendUiState
import com.bff.wespot.model.common.KakaoSharingType
import com.bff.wespot.model.common.Paging
import com.bff.wespot.model.message.request.WrittenMessage
import com.bff.wespot.model.user.response.User
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
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
import javax.inject.Inject

@HiltViewModel
class SendViewModel @Inject constructor(
    private val messageRepository: MessageRepository,
    private val profileRepository: ProfileRepository,
    private val commonRepository: CommonRepository,
    private val userListRepository: BasePagingRepository<User, Paging<User>>,
    private val checkProfanityUseCase: CheckProfanityUseCase,
    private val coroutineDispatcher: CoroutineDispatcher,
) : ViewModel(), ContainerHost<SendUiState, SendSideEffect> {
    override val container = container<SendUiState, SendSideEffect>(SendUiState())

    private val nameInput: MutableStateFlow<String> = MutableStateFlow("")
    private val messageInput: MutableStateFlow<String> = MutableStateFlow("")
    private val randomNameGenerator by lazy { RandomNameGenerator() }

    fun onAction(action: SendAction) {
        when (action) {
            is SendAction.OnReceiverScreenEntered -> {
                getKakaoContent()
                observeNameInput()
            }
            is SendAction.OnMessageEditScreenEntered -> {
                handleMessageEditScreenEntered(action.isReservedMessage, action.messageId)
            }

            is SendAction.OnWriteScreenEntered -> observeMessageInput()
            is SendAction.OnSearchContentChanged -> handleSearchContentChanged(action.content)
            is SendAction.OnUserSelected -> handleUserSelected(action.user)
            is SendAction.OnMessageChanged -> handleMessageChanged(action.content)
            is SendAction.OnSendButtonClicked -> handleMessageSent()
            is SendAction.OnRandomNameToggled -> handleRandomNameToggled()
            is SendAction.OnEditButtonClicked -> handleEditButtonClicked(action.messageId)
            SendAction.OnInviteFriendTextClicked -> {}
            SendAction.OnReservedMessageScreenEntered, SendAction.OnMessageScreenEntered -> {
                clearSendUiState()
            }
        }
    }

    private fun handleSearchContentChanged(content: String) = intent {
        reduce {
            nameInput.value = content
            state.copy(
                nameInput = content,
            )
        }
    }

    private fun handleUserSelected(user: User) = intent {
        reduce {
            state.copy(
                selectedUser = user,
            )
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

    private fun handleMessageEditScreenEntered(
        isReservedMessage: Boolean,
        messageId: Int,
    ) = intent {
        // 기존 상태가 존재하는 경우 재호출하지 않는다.
        if (state.sender.isNotEmpty()) {
            return@intent
        }

        if (isReservedMessage) {
            reduce {
                state.copy(isReservedMessage = true, messageId = messageId)
            }
            getReservedMessage(state.messageId)
        } else {
            getProfile()
        }
    }

    private fun getReservedMessage(messageId: Int) = intent {
        viewModelScope.launch {
            messageRepository.getMessage(messageId)
                .onSuccess { message ->
                    reduce {
                        state.copy(
                            selectedUser = message.receiver,
                            messageInput = message.content,
                            isRandomName = message.isAnonymous,
                            sender = message.senderName,
                        )
                    }
                    // 예약된 메세지 보낸이가 익명인 경우, 새로 프로필을 불러온다.
                    if (message.isAnonymous) {
                        getProfile()
                    }

                    messageInput.value = message.content
                }
        }
    }

    private fun getProfile() = intent {
        viewModelScope.launch {
            runCatching {
                profileRepository.getProfile()
            }.onSuccess { profile ->
                reduce { state.copy(sender = profile.toDescription()) }
            }
        }
    }

    private fun handleRandomNameToggled() = intent {
        reduce {
            state.copy(
                isRandomName = state.isRandomName.not(),
                randomName = randomNameGenerator.getRandomName(),
            )
        }
    }

    private fun handleMessageSent() = intent {
        reduce { state.copy(isLoading = true) }
        postSideEffect(SendSideEffect.CloseReserveDialog)

        viewModelScope.launch {
            messageRepository.postMessage(
                WrittenMessage(
                    receiverId = state.selectedUser.id,
                    content = state.messageInput,
                    senderName = if (state.isRandomName) state.randomName else state.sender,
                    isAnonymous = state.isRandomName,
                ),
            ).onSuccess {
                reduce { state.copy(isLoading = false) }
                postSideEffect(SendSideEffect.NavigateToMessage)
            }.onNetworkFailure { exception ->
                if (exception.status == 400) { // TODO 나중에 추가 필드로 구분 예정
                    reduce { state.copy(messageSendFailedDialogContent = exception.detail) }
                    postSideEffect(SendSideEffect.ShowTimeoutDialog)
                }
            }.onFailure {
                reduce { state.copy(isLoading = false) }
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

    private fun handleEditButtonClicked(messageId: Int) = intent {
        viewModelScope.launch {
            messageRepository.editMessage(
                messageId = messageId,
                WrittenMessage(
                    receiverId = state.selectedUser.id,
                    content = state.messageInput,
                    senderName = if (state.isRandomName) state.randomName else state.sender,
                    isAnonymous = state.isRandomName,
                ),
            ).onSuccess {
                postSideEffect(SendSideEffect.NavigateToReservedMessage)
            }.onNetworkFailure { exception ->
                if (exception.status == 400) {
                    postSideEffect(SendSideEffect.ShowTimeoutDialog)
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
                .onFailure {
                    Timber.e(it)
                }
        }
    }

    private fun clearSendUiState() = intent {
        reduce {
            SendUiState()
        }
        nameInput.value = ""
        messageInput.value = ""
    }

    companion object {
        private const val INPUT_DEBOUNCE_TIME = 500L
    }
}

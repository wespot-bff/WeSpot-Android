package com.bff.wespot.message.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bff.wespot.common.di.extensions.onNetworkFailure
import com.bff.wespot.common.util.RandomNameGenerator
import com.bff.wespot.domain.repository.message.MessageRepository
import com.bff.wespot.domain.repository.user.UserRepository
import com.bff.wespot.domain.usecase.CheckProfanityUseCase
import com.bff.wespot.message.common.MESSAGE_MAX_LENGTH
import com.bff.wespot.message.state.send.SendAction
import com.bff.wespot.message.state.send.SendSideEffect
import com.bff.wespot.message.state.send.SendUiState
import com.bff.wespot.model.message.request.SentMessage
import com.bff.wespot.model.user.response.User
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
import javax.inject.Inject

@HiltViewModel
class SendViewModel @Inject constructor(
    private val messageRepository: MessageRepository,
    private val userRepository: UserRepository,
    private val checkProfanityUseCase: CheckProfanityUseCase,
) : ViewModel(), ContainerHost<SendUiState, SendSideEffect> {
    override val container = container<SendUiState, SendSideEffect>(SendUiState())

    private val nameInput: MutableStateFlow<String> = MutableStateFlow("")
    private val messageInput: MutableStateFlow<String> = MutableStateFlow("")
    private val randomNameGenerator by lazy { RandomNameGenerator() }

    fun onAction(action: SendAction) {
        when (action) {
            is SendAction.OnReceiverScreenEntered -> observeNameInput()
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
                            randomName = message.senderName,
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
            userRepository.getProfile()
                .onSuccess { profile ->
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
        viewModelScope.launch {
            messageRepository.postMessage(
                SentMessage(
                    receiverId = state.selectedUser.id,
                    content = state.messageInput,
                    sender = if (state.isRandomName) state.randomName else state.sender,
                    isAnonymous = state.isRandomName,
                ),
            ).onSuccess {
                postSideEffect(SendSideEffect.NavigateToMessage)
            }.onNetworkFailure { exception ->
                if (exception.status == 400) { // TODO 나중에 추가 필드로 구분 예정
                    postSideEffect(SendSideEffect.ShowTimeoutDialog)
                }
            }
        }
    }

    private fun getUserList(name: String) = intent {
        viewModelScope.launch {
            userRepository.getUserListByName(name, cursorId = 0) // TODO 커서 페이징 구현
                .onSuccess { userList ->
                    reduce {
                        state.copy(
                            userList = userList,
                        )
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

    private fun handleEditButtonClicked(messageId: Int) = intent {
        viewModelScope.launch {
            messageRepository.editMessage(
                messageId = messageId,
                SentMessage(
                    receiverId = state.selectedUser.id,
                    content = state.messageInput,
                    sender = if (state.isRandomName) state.randomName else state.sender,
                    isAnonymous = state.isRandomName,
                ),
            ).onSuccess {
                postSideEffect(SendSideEffect.NavigateToReservedMessage)
            }.onNetworkFailure { exception ->
                if (exception.status == 400) { // TODO 나중에 추가 필드로 구분 예정
                    postSideEffect(SendSideEffect.ShowTimeoutDialog)
                }
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

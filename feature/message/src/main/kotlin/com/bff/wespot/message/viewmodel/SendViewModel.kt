package com.bff.wespot.message.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bff.wespot.common.di.extensions.onNetworkFailure
import com.bff.wespot.common.util.RandomNameGenerator
import com.bff.wespot.domain.repository.message.MessageRepository
import com.bff.wespot.domain.repository.user.UserRepository
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
) : ViewModel(), ContainerHost<SendUiState, SendSideEffect> {
    override val container = container<SendUiState, SendSideEffect>(SendUiState())

    private val nameInput: MutableStateFlow<String> = MutableStateFlow("")
    private val messageInput: MutableStateFlow<String> = MutableStateFlow("")
    private val randomNameGenerator by lazy { RandomNameGenerator() }

    fun onAction(action: SendAction) {
        when (action) {
            is SendAction.OnReceiverScreenEntered -> observeNameInput()
            is SendAction.OnWriteScreenEntered -> observeMessageInput()
            is SendAction.OnSearchContentChanged -> handleSearchContentChanged(action.content)
            is SendAction.OnUserSelected -> handleUserSelected(action.user)
            is SendAction.OnMessageChanged -> handleMessageChanged(action.content)
            is SendAction.SendMessage -> handleMessageSent()
            is SendAction.OnRandomNameToggled -> handleRandomNameToggled()
            is SendAction.NavigateToMessage -> clearSendUiState()
            is SendAction.OnMessageEditScreenEntered -> getProfile()
            else -> {}
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
                .collect {
                    getUserList(it)
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

    private fun getProfile() = intent {
        viewModelScope.launch {
            userRepository.getProfile()
                .onSuccess { profile ->
                    reduce {
                        state.copy(
                            profile = profile,
                        )
                    }
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
                    sender = if (state.isRandomName) {
                        state.randomName
                    } else {
                        state.profile.toDescription()
                    },
                ),
            ).onSuccess {
                postSideEffect(SendSideEffect.NavigateToMessage)
            }.onNetworkFailure { exception ->
                if (exception.status == 400) {
                    postSideEffect(SendSideEffect.ShowTimeoutDialog)
                }
            }
        }
    }

    private fun getUserList(name: String) = intent {
        viewModelScope.launch {
            userRepository.getUserListByName(name)
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
            messageRepository.checkProfanity(content)
                .onSuccess {
                    reduce {
                        state.copy(
                            hasProfanity = false,
                        )
                    }
                }
                .onNetworkFailure { exception ->
                    if (exception.status == 400) {
                        reduce {
                            state.copy(
                                hasProfanity = true,
                            )
                        }
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

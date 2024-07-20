package com.bff.wespot.message.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bff.wespot.domain.repository.message.MessageRepository
import com.bff.wespot.message.model.TimePeriod
import com.bff.wespot.message.model.getCurrentTimePeriod
import com.bff.wespot.message.state.MessageAction
import com.bff.wespot.message.state.MessageSideEffect
import com.bff.wespot.message.state.MessageUiState
import com.bff.wespot.message.state.NavigationAction
import com.bff.wespot.model.message.request.MessageType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.postSideEffect
import org.orbitmvi.orbit.syntax.simple.reduce
import org.orbitmvi.orbit.viewmodel.container
import javax.inject.Inject

@HiltViewModel
class MessageViewModel @Inject constructor(
    private val messageRepository: MessageRepository,
) : ViewModel(), ContainerHost<MessageUiState, MessageSideEffect> {
    override val container = container<MessageUiState, MessageSideEffect>(MessageUiState())

    fun onAction(action: MessageAction) {
        when (action) {
            is MessageAction.OnHomeScreenEntered -> handleHomeScreenEntered()
            is MessageAction.Navigation -> handleNavigation(action.navigate)
            else -> {}
        }
    }

    private fun handleHomeScreenEntered() = intent {
        val currentTimePeriod = getCurrentTimePeriod()
        reduce {
            state.copy(
                timePeriod = currentTimePeriod,
            )
        }

        when (currentTimePeriod) {
            TimePeriod.DAWN_TO_EVENING -> {
            }

            TimePeriod.EVENING_TO_NIGHT -> {
                getMessageStatus()
            }

            TimePeriod.NIGHT_TO_DAWN -> {
                getReceivedMessageList()
            }
        }
    }

    private fun handleNavigation(navigate: NavigationAction) = intent {
        val sideEffect = when (navigate) {
            NavigationAction.NavigateToSendScreen -> {
                MessageSideEffect.NavigateToSendScreen
            }

            NavigationAction.NavigateToStorageScreen -> {
                MessageSideEffect.NavigateToStorageScreen
            }
        }
        postSideEffect(sideEffect)
    }

    private fun getMessageStatus() = intent {
        viewModelScope.launch {
            messageRepository.getMessageStatus()
                .onSuccess { messageStatus ->
                    reduce {
                        state.copy(
                            messageStatus = messageStatus,
                        )
                    }
                }
                .onFailure { exception ->
                    postSideEffect(MessageSideEffect.Error(exception))
                }
        }
    }

    private fun getReceivedMessageList() = intent {
        viewModelScope.launch {
            messageRepository.getMessageList(MessageType.RECEIVED)
                .onSuccess { receivedMessageList ->
                    reduce {
                        state.copy(
                            receivedMessageList = receivedMessageList,
                        )
                    }
                }
                .onFailure { exception ->
                    postSideEffect(MessageSideEffect.Error(exception))
                }
        }
    }
}

package com.bff.wespot.message.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bff.wespot.domain.repository.message.MessageRepository
import com.bff.wespot.domain.repository.user.UserRepository
import com.bff.wespot.message.model.TimePeriod
import com.bff.wespot.message.model.getCurrentTimePeriod
import com.bff.wespot.message.state.MessageAction
import com.bff.wespot.message.state.MessageSideEffect
import com.bff.wespot.message.state.MessageUiState
import com.bff.wespot.model.message.request.MessageType
import com.bff.wespot.model.message.response.Message
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.reduce
import org.orbitmvi.orbit.viewmodel.container
import javax.inject.Inject

@HiltViewModel
class MessageViewModel @Inject constructor(
    private val coroutineDispatcher: CoroutineDispatcher,
    private val messageRepository: MessageRepository,
    private val userRepository: UserRepository,
) : ViewModel(), ContainerHost<MessageUiState, MessageSideEffect> {
    override val container = container<MessageUiState, MessageSideEffect>(MessageUiState())

    private val _remainingTimeMillis: MutableStateFlow<Long> = MutableStateFlow(0)
    val remainingTimeMillis: StateFlow<Long> = _remainingTimeMillis.asStateFlow()

    private var previousTimeMills: Long = 0
    private val timerJob: Job = viewModelScope.launch(start = CoroutineStart.LAZY) {
        withContext(coroutineDispatcher) {
            previousTimeMills = System.currentTimeMillis()
            while (_remainingTimeMillis.value > 0L) {
                val delayMills = System.currentTimeMillis() - previousTimeMills
                if (delayMills == 1000L) {
                    _remainingTimeMillis.value = (_remainingTimeMillis.value - delayMills)
                    previousTimeMills = System.currentTimeMillis()
                }
            }
        }
    }

    private val timeChecker: Job = viewModelScope.launch(start = CoroutineStart.LAZY) {
        withContext(coroutineDispatcher) {
            while (true) {
                delay(1000)
                intent {
                    // TimePeriod가 변경되는 경우, UI 업데이트 수행
                    val currentTimePeriod = getCurrentTimePeriod()
                    if (state.timePeriod != currentTimePeriod) {
                        updateTimePeriod(currentTimePeriod)
                    }
                }
            }
        }
    }

    fun onAction(action: MessageAction) {
        when (action) {
            is MessageAction.OnHomeScreenEntered -> handleHomeScreenEntered()
            is MessageAction.OnMessageStorageScreenEntered -> getProfile()
            is MessageAction.OnStorageChipSelected -> {
                when (action.messageType) {
                    MessageType.SENT -> {
                        getMessageStatus()
                        getSentMessageList()
                    }
                    MessageType.RECEIVED -> getReceivedMessageList()
                }
            }
            is MessageAction.OnMessageItemClicked -> handleMessageItemClicked(action.message)
        }
    }

    private fun handleHomeScreenEntered() {
        val currentTimePeriod = getCurrentTimePeriod()
        updateTimePeriod(currentTimePeriod)
        timeChecker.start()
    }

    private fun getProfile() = intent {
        viewModelScope.launch {
            userRepository.getProfile()
                .onSuccess { profile ->
                    reduce {
                        state.copy(
                            myProfile = profile,
                        )
                    }
                }
        }
    }

    private fun updateTimePeriod(currentTimePeriod: TimePeriod) {
        intent {
            reduce {
                state.copy(
                    timePeriod = currentTimePeriod,
                )
            }
        }

        when (currentTimePeriod) {
            TimePeriod.DAWN_TO_EVENING -> {
            }

            TimePeriod.EVENING_TO_NIGHT -> {
                getMessageStatus()
                startTimer()
            }

            TimePeriod.NIGHT_TO_DAWN -> {
                getReceivedMessageList()
            }
        }
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
        }
    }

    private fun getSentMessageList() = intent {
        viewModelScope.launch {
            messageRepository.getMessageList(MessageType.SENT)
                .onSuccess { sentMessageList ->
                    reduce {
                        state.copy(
                            sentMessageList = sentMessageList,
                        )
                    }
                }
        }
    }

    private fun handleMessageItemClicked(message: Message) = intent {
        reduce {
            state.copy(
                clickedMessage = message,
            )
        }
        updateMessageReadStatus(message.id)
    }

    private fun updateMessageReadStatus(messageId: Int) {
        viewModelScope.launch {
            messageRepository.updateMessageReadStatus(messageId)
        }
    }

    private fun startTimer() {
        _remainingTimeMillis.value = getRemainingTimeMillis()
        if (!timerJob.isActive) {
            timerJob.start()
        }
    }

    private fun getRemainingTimeMillis(): Long {
        val currentTimeMillis = System.currentTimeMillis() + MILLIS_KTC_OFFSET
        val elapsedMillis = currentTimeMillis % MILLIS_PER_DAY

        return if (elapsedMillis <= MILLIS_TO_TEN_PM) MILLIS_TO_TEN_PM - elapsedMillis else 0L
    }

    companion object {
        private const val MILLIS_PER_DAY = 24 * 3600 * 1000
        private const val MILLIS_TO_TEN_PM = 22 * 3600 * 1000
        private const val MILLIS_KTC_OFFSET = 9 * 3600 * 1000
    }
}

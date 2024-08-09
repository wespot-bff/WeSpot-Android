package com.bff.wespot.message.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bff.wespot.domain.repository.CommonRepository
import com.bff.wespot.domain.repository.message.MessageRepository
import com.bff.wespot.domain.repository.message.MessageStorageRepository
import com.bff.wespot.message.model.MessageOptionType
import com.bff.wespot.message.model.TimePeriod
import com.bff.wespot.message.model.getCurrentTimePeriod
import com.bff.wespot.message.state.MessageAction
import com.bff.wespot.message.state.MessageSideEffect
import com.bff.wespot.message.state.MessageUiState
import com.bff.wespot.model.common.ReportType
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
import org.orbitmvi.orbit.syntax.simple.postSideEffect
import org.orbitmvi.orbit.syntax.simple.reduce
import org.orbitmvi.orbit.viewmodel.container
import javax.inject.Inject

@HiltViewModel
class MessageViewModel @Inject constructor(
    private val coroutineDispatcher: CoroutineDispatcher,
    private val messageRepository: MessageRepository,
    private val messageStorageRepository: MessageStorageRepository,
    private val commonRepository: CommonRepository,
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
            is MessageAction.OnOptionButtonClicked -> handleOptionButtonClicked(action.message)
            is MessageAction.OnOptionBottomSheetClicked -> {
                handleOptionBottomSheetClicked(action.messageOptionType)
            }
            is MessageAction.OnMessageDeleteButtonClicked -> {
                handleDeleteMessageButtonClicked(action.messageId)
            }
            is MessageAction.OnMessageReportButtonClicked -> {
                handleReportMessageButtonClicked(action.messageId)
            }
            is MessageAction.OnMessageBlockButtonClicked -> {
                handleBlockMessageButtonClicked(action.messageId)
            }
            MessageAction.OnReservedMessageScreenEntered -> handleReservedMessageScreenEntered()
        }
    }

    private fun handleHomeScreenEntered() {
        val currentTimePeriod = getCurrentTimePeriod()
        updateTimePeriod(currentTimePeriod)
        timeChecker.start()
    }

    private fun handleReservedMessageScreenEntered() = intent {
        viewModelScope.launch {
            messageStorageRepository.getReservedMessage()
                .onSuccess { reservedMessageList ->
                    reduce { state.copy(reservedMessageList = reservedMessageList) }
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
            messageRepository.getMessageList(MessageType.RECEIVED, cursorId = 0) // TODO 커서 구현
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
            messageRepository.getMessageList(MessageType.SENT, cursorId = 0) // TODO 커서 페이징 구현
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

        if (message.isRead.not()) {
            updateMessageReadStatus(messageId = message.id)
        }
    }

    private fun handleOptionButtonClicked(message: Message) = intent {
        reduce {
            state.copy(
                optionButtonClickedMessage = message,
            )
        }
    }

    private fun handleOptionBottomSheetClicked(messageOptionType: MessageOptionType) = intent {
        reduce {
            state.copy(messageOptionType = messageOptionType)
        }
    }

    private fun updateMessageReadStatus(messageId: Int) {
        viewModelScope.launch {
            messageStorageRepository.updateMessageReadStatus(messageId)
                .onSuccess {
                    getReceivedMessageList()
                }
        }
    }

    private fun handleDeleteMessageButtonClicked(messageId: Int) = intent {
        viewModelScope.launch {
            messageStorageRepository.deleteMessage(messageId)
                .onSuccess {
                    postSideEffect(MessageSideEffect.ShowToast("삭제 완료"))
                    getReceivedMessageList()
                }
        }
    }

    private fun handleReportMessageButtonClicked(messageId: Int) = intent {
        viewModelScope.launch {
            commonRepository.sendReport(ReportType.MESSAGE, messageId)
                .onSuccess {
                    postSideEffect(MessageSideEffect.ShowToast("신고 완료"))
                    getReceivedMessageList()
                }
        }
    }

    private fun handleBlockMessageButtonClicked(messageId: Int) = intent {
        viewModelScope.launch {
            messageStorageRepository.blockMessage(messageId)
                .onSuccess {
                    postSideEffect(MessageSideEffect.ShowToast("차단 완료"))
                    getReceivedMessageList()
                }
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

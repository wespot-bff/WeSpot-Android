package com.bff.wespot.message.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.bff.wespot.designsystem.component.indicator.WSToastType
import com.bff.wespot.domain.repository.BasePagingRepository
import com.bff.wespot.domain.repository.CommonRepository
import com.bff.wespot.domain.repository.RemoteConfigRepository
import com.bff.wespot.domain.repository.message.MessageRepository
import com.bff.wespot.domain.repository.message.MessageStorageRepository
import com.bff.wespot.domain.util.RemoteConfigKey
import com.bff.wespot.message.R
import com.bff.wespot.message.model.ClickedMessageUiModel
import com.bff.wespot.message.model.MessageOptionType
import com.bff.wespot.message.model.TimePeriod
import com.bff.wespot.message.model.getCurrentTimePeriod
import com.bff.wespot.message.state.MessageAction
import com.bff.wespot.message.state.MessageSideEffect
import com.bff.wespot.message.state.MessageUiState
import com.bff.wespot.model.ToastState
import com.bff.wespot.model.common.Paging
import com.bff.wespot.model.common.ReportType
import com.bff.wespot.model.message.request.MessageType
import com.bff.wespot.model.message.response.ReceivedMessage
import com.bff.wespot.model.message.response.SentMessage
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
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class MessageViewModel @Inject constructor(
    private val coroutineDispatcher: CoroutineDispatcher,
    private val messageRepository: MessageRepository,
    private val messageStorageRepository: MessageStorageRepository,
    private val messageReceivedRepository: BasePagingRepository<ReceivedMessage, Paging<ReceivedMessage>>,
    private val messageSentRepository: BasePagingRepository<SentMessage, Paging<SentMessage>>,
    private val commonRepository: CommonRepository,
    remoteConfigRepository: RemoteConfigRepository,
) : ViewModel(), ContainerHost<MessageUiState, MessageSideEffect> {
    override val container = container<MessageUiState, MessageSideEffect>(
        MessageUiState(
            messageStartTime = remoteConfigRepository.fetchFromRemoteConfig(
                RemoteConfigKey.MESSAGE_START_TIME,
            ),
            messageReceiveTime = remoteConfigRepository.fetchFromRemoteConfig(
                RemoteConfigKey.MESSAGE_RECEIVE_TIME,
            ),
        ),
    )

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
                    val currentTimePeriod = getCurrentTimePeriod(
                        messageStartTime = state.messageStartTime,
                        messageReceiveTime = state.messageReceiveTime,
                    )
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
            is MessageAction.OnMessageStorageScreenOpened -> {
                handleMessageStorageScreenOpened(action.messageId, action.type)
            }
            is MessageAction.OnSentMessageClicked ->
                handleSentMessageClicked(action.message)
            is MessageAction.OnReceivedMessageClicked ->
                handleReceivedMessageClicked(action.message)
            is MessageAction.OnOptionButtonClicked ->
                handleOptionButtonClicked(action.messageId, action.messageType)
            is MessageAction.OnOptionBottomSheetClicked -> {
                handleOptionBottomSheetClicked(action.messageOptionType)
            }
            MessageAction.OnMessageDeleteButtonClicked -> {
                handleDeleteMessageButtonClicked()
            }
            MessageAction.OnMessageReportButtonClicked -> {
                handleReportMessageButtonClicked()
            }
            MessageAction.OnMessageBlockButtonClicked -> {
                handleBlockMessageButtonClicked()
            }
            MessageAction.OnReservedMessageScreenEntered -> handleReservedMessageScreenEntered()
        }
    }

    private fun handleHomeScreenEntered() = intent {
        val currentTimePeriod = getCurrentTimePeriod(
            messageStartTime = state.messageStartTime,
            messageReceiveTime = state.messageReceiveTime,
        )
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
                getMessageStatus()
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
        viewModelScope.launch(coroutineDispatcher) {
            runCatching {
                reduce {
                    state.copy(
                        receivedMessageList = messageReceivedRepository.fetchResultStream()
                            .cachedIn(viewModelScope),
                    )
                }
            }.onFailure { exception ->
                Timber.e(exception)
            }
        }
    }

    private fun getSentMessageList() = intent {
        viewModelScope.launch(coroutineDispatcher) {
            runCatching {
                reduce {
                    state.copy(
                        sentMessageList = messageSentRepository.fetchResultStream()
                            .cachedIn(viewModelScope),
                    )
                }
            }.onFailure { exception ->
                Timber.e(exception)
            }
        }
    }

    private fun handleMessageStorageScreenOpened(messageId: Int, type: MessageType) = intent {
        reduce { state.copy(isLoading = true) }
        viewModelScope.launch {
            messageRepository.getMessage(messageId)
                .onSuccess { message ->
                    when (type) {
                        MessageType.SENT -> handleSentMessageClicked(message.toSentMessage())
                        MessageType.RECEIVED -> handleReceivedMessageClicked(
                            message.toReceivedMessage(),
                        )
                    }
                    reduce { state.copy(isLoading = false) }
                    postSideEffect(MessageSideEffect.ShowMessageDialog)
                }
                .onFailure {
                    reduce { state.copy(isLoading = false) }
                }
        }
    }

    private fun handleReceivedMessageClicked(message: ReceivedMessage) = intent {
        reduce {
            state.copy(
                clickedMessage = ClickedMessageUiModel(
                    content = message.content,
                    sender = message.senderName,
                    receiver = message.receiver.toDescription(),
                ),
            )
        }

        if (message.isRead.not()) {
            updateMessageReadStatus(messageId = message.id)
        }
    }

    private fun handleSentMessageClicked(message: SentMessage) = intent {
        reduce {
            state.copy(
                clickedMessage = ClickedMessageUiModel(
                    content = message.content,
                    sender = message.senderName,
                    receiver = message.receiver.toDescription(),
                ),
            )
        }
    }

    private fun handleOptionButtonClicked(messageId: Int, messageType: MessageType) = intent {
        reduce {
            state.copy(
                optionButtonClickedMessageId = messageId,
                optionButtonClickedMessageType = messageType,
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

    private fun handleDeleteMessageButtonClicked() = intent {
        viewModelScope.launch {
            messageStorageRepository.deleteMessage(state.optionButtonClickedMessageId)
                .onSuccess {
                    when (state.optionButtonClickedMessageType) {
                        MessageType.RECEIVED -> getReceivedMessageList()
                        MessageType.SENT -> getSentMessageList()
                    }

                    postSideEffect(
                        MessageSideEffect.ShowToast(
                            ToastState(
                                show = true,
                                message = R.string.delete_done,
                                type = WSToastType.Success,
                            ),
                        ),
                    )
                }
        }
    }

    private fun handleReportMessageButtonClicked() = intent {
        viewModelScope.launch {
            commonRepository.sendReport(ReportType.MESSAGE, state.optionButtonClickedMessageId)
                .onSuccess {
                    postSideEffect(
                        MessageSideEffect.ShowToast(
                            ToastState(
                                show = true,
                                message = R.string.report_done,
                                type = WSToastType.Success,
                            ),
                        ),
                    )
                    getReceivedMessageList()
                }
        }
    }

    private fun handleBlockMessageButtonClicked() = intent {
        viewModelScope.launch {
            messageStorageRepository.blockMessage(state.optionButtonClickedMessageId)
                .onSuccess {
                    postSideEffect(
                        MessageSideEffect.ShowToast(
                            ToastState(
                                show = true,
                                message = R.string.block_done,
                                type = WSToastType.Success,
                            ),
                        ),
                    )
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

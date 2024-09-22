package com.bff.wespot.message.viewmodel

import androidx.lifecycle.viewModelScope
import com.bff.wespot.base.BaseViewModel
import com.bff.wespot.domain.repository.RemoteConfigRepository
import com.bff.wespot.domain.repository.message.MessageRepository
import com.bff.wespot.domain.repository.message.MessageStorageRepository
import com.bff.wespot.domain.util.RemoteConfigKey
import com.bff.wespot.message.model.TimePeriod
import com.bff.wespot.message.model.getCurrentTimePeriod
import com.bff.wespot.message.state.MessageAction
import com.bff.wespot.message.state.MessageSideEffect
import com.bff.wespot.message.state.MessageUiState
import dagger.hilt.android.lifecycle.HiltViewModel
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
    private val messageRepository: MessageRepository,
    private val messageStorageRepository: MessageStorageRepository,
    remoteConfigRepository: RemoteConfigRepository,
) : BaseViewModel(), ContainerHost<MessageUiState, MessageSideEffect> {
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

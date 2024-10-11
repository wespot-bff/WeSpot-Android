package com.bff.wespot.message.viewmodel

import androidx.lifecycle.viewModelScope
import com.bff.wespot.common.extension.onNetworkFailure
import com.bff.wespot.domain.repository.firebase.config.RemoteConfigRepository
import com.bff.wespot.domain.repository.message.MessageRepository
import com.bff.wespot.domain.repository.message.MessageStorageRepository
import com.bff.wespot.domain.util.RemoteConfigKey
import com.bff.wespot.message.model.TimePeriod
import com.bff.wespot.message.model.getCurrentTimePeriod
import com.bff.wespot.message.state.MessageAction
import com.bff.wespot.message.state.MessageSideEffect
import com.bff.wespot.message.state.MessageUiState
import com.bff.wespot.ui.base.BaseViewModel
import com.bff.wespot.ui.model.SideEffect.Companion.toSideEffect
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.isActive
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
        ).let { state ->
            state.copy(
                timePeriod = getCurrentTimePeriod(state.messageStartTime, state.messageReceiveTime),
            )
        },
    )

    private val _remainingTimeMillis: MutableStateFlow<Long> = MutableStateFlow(0)
    val remainingTimeMillis: StateFlow<Long> = _remainingTimeMillis.asStateFlow()

    private var previousTimeMills: Long = 0
    private val timerJob: Job = viewModelScope.launch(start = CoroutineStart.LAZY) {
        withContext(coroutineDispatcher) {
            previousTimeMills = System.currentTimeMillis()
            while (isActive) {
                val delayMills = System.currentTimeMillis() - previousTimeMills
                if (delayMills == 1000L) {
                    intent {
                        val currentTimePeriod = getCurrentTimePeriod(
                            messageStartTime = state.messageStartTime,
                            messageReceiveTime = state.messageReceiveTime,
                        )

                        updateTimePeriod(currentTimePeriod)

                        // TimerPeriod 상태에 따라 타이머 시각 상태 변경
                        if (currentTimePeriod == TimePeriod.EVENING_TO_NIGHT) {
                            _remainingTimeMillis.value = (_remainingTimeMillis.value - delayMills)
                        }
                    }
                    previousTimeMills = System.currentTimeMillis()
                }
            }
        }
    }

    fun onAction(action: MessageAction) {
        when (action) {
            MessageAction.StartTimeTracking -> startTimer()
            MessageAction.CancelTimeTracking -> cancelTimer()
            MessageAction.OnReservedMessageScreenEntered -> handleReservedMessageScreenEntered()
        }
    }

    private fun getMessageStatus() = intent {
        viewModelScope.launch {
            messageRepository.getMessageStatus()
                .onSuccess { messageStatus ->
                    reduce {
                        state.copy(messageStatus = messageStatus)
                    }
                }.onNetworkFailure {
                    postSideEffect(it.toSideEffect())
                }
        }
    }

    private fun startTimer() = intent {
        if (!timerJob.isActive) {
            setTimer()
            timerJob.start()
        }
    }

    private fun cancelTimer() {
        timerJob.cancel()
    }

    private fun handleReservedMessageScreenEntered() = intent {
        viewModelScope.launch {
            reduce { state.copy(isLoading = true) }
            messageStorageRepository.getReservedMessage()
                .onSuccess { reservedMessageList ->
                    reduce {
                        state.copy(
                            reservedMessageList = reservedMessageList,
                            isLoading = false,
                        )
                    }
                }
                .onNetworkFailure {
                    postSideEffect(it.toSideEffect())
                }
                .onFailure {
                    reduce { state.copy(isLoading = false) }
                }
        }
    }

    private fun updateTimePeriod(currentTimePeriod: TimePeriod) = intent {
        if (state.timePeriod != currentTimePeriod) {
            reduce {
                state.copy(timePeriod = currentTimePeriod)
            }
            setTimer()
        }
    }

    private fun setTimer() = intent {
        getMessageStatus()

        if (state.timePeriod == TimePeriod.EVENING_TO_NIGHT) {
            _remainingTimeMillis.value = getRemainingTimeMillis()
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

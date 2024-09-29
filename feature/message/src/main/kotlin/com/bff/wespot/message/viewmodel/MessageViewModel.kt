package com.bff.wespot.message.viewmodel

import androidx.lifecycle.viewModelScope
import com.bff.wespot.base.BaseViewModel
import com.bff.wespot.common.extension.onNetworkFailure
import com.bff.wespot.domain.repository.RemoteConfigRepository
import com.bff.wespot.domain.repository.message.MessageRepository
import com.bff.wespot.domain.repository.message.MessageStorageRepository
import com.bff.wespot.domain.util.RemoteConfigKey
import com.bff.wespot.message.model.TimePeriod
import com.bff.wespot.message.model.getCurrentTimePeriod
import com.bff.wespot.message.state.MessageAction
import com.bff.wespot.message.state.MessageSideEffect
import com.bff.wespot.message.state.MessageUiState
import com.bff.wespot.model.SideEffect.Companion.toSideEffect
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
        ),
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
                .onFailure {
                    reduce { state.copy(isLoading = false) }
                }
        }
    }

    /**
     * TimePeriod Default 값은 DAWN_TO_EVENING으로
     * DAWN_TO_EVENING 상태에서 다른 행동을 수행하지 않기 때문에, TimePeriod가 변경되었을 때만 행동을 수행한다.
     */
    private fun updateTimePeriod(currentTimePeriod: TimePeriod) = intent {
        if (state.timePeriod != currentTimePeriod) {
            getMessageStatus()

            // 메세지 전송 가능한 시간이 경우, 타이머 시각을 설정한다.
            if (currentTimePeriod == TimePeriod.EVENING_TO_NIGHT) {
                _remainingTimeMillis.value = getRemainingTimeMillis()
            }

            reduce {
                state.copy(timePeriod = currentTimePeriod)
            }
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

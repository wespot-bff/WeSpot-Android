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
import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.Dispatchers
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
    private val messageRepository: MessageRepository,
) : ViewModel(), ContainerHost<MessageUiState, MessageSideEffect> {
    override val container = container<MessageUiState, MessageSideEffect>(MessageUiState())

    private val _remainingTimeMillis: MutableStateFlow<Long> = MutableStateFlow(0)
    val remainingTimeMillis: StateFlow<Long> = _remainingTimeMillis.asStateFlow()

    private var previousTimeMills: Long = 0
    private val timerJob: Job = viewModelScope.launch(start = CoroutineStart.LAZY) {
        withContext(Dispatchers.IO) {
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
        withContext(Dispatchers.IO) {
            while (true) {
                updateTimePeriod()
                delay(1000)
            }
        }
    }

    fun onAction(action: MessageAction) {
        when (action) {
            is MessageAction.OnHomeScreenEntered -> handleHomeScreenEntered()
            is MessageAction.Navigation -> handleNavigation(action.navigate)
            else -> {}
        }
    }

    private fun handleHomeScreenEntered() {
        timeChecker.start()
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

    private fun updateTimePeriod() {
        val currentTimePeriod = getCurrentTimePeriod()
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
        private const val MILLIS_KTC_OFFSET = 6 * 3600 * 1000
    }
}

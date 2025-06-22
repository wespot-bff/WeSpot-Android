package com.bff.wespot.message.viewmodel

import androidx.lifecycle.viewModelScope
import com.bff.wespot.common.extension.onNetworkFailure
import com.bff.wespot.domain.repository.message.MessageRepository
import com.bff.wespot.domain.repository.user.ProfileRepository
import com.bff.wespot.domain.usecase.GetMessageHomeTitleUseCase
import com.bff.wespot.message.state.home.MessageHomeAction
import com.bff.wespot.message.state.home.MessageHomeSideEffect
import com.bff.wespot.message.state.home.MessageHomeUiState
import com.bff.wespot.ui.base.BaseViewModel
import com.bff.wespot.ui.model.SideEffect.Companion.toSideEffect
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.reduce
import org.orbitmvi.orbit.viewmodel.container
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class MessageHomeViewModel @Inject constructor(
    private val messageRepository: MessageRepository,
    private val profileRepository: ProfileRepository,
    private val getMessageHomeTitle: GetMessageHomeTitleUseCase,
) : BaseViewModel(), ContainerHost<MessageHomeUiState, MessageHomeSideEffect> {
    override val container = container<MessageHomeUiState, MessageHomeSideEffect>(MessageHomeUiState()) {
        observeProfileFlow()
        getMessageHomeTitle()
    }

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
                        _remainingTimeMillis.value = (_remainingTimeMillis.value - delayMills)
                    }
                    previousTimeMills = System.currentTimeMillis()
                }
            }
        }
    }

    fun onAction(action: MessageHomeAction) {
        when (action) {
            MessageHomeAction.OnScreenEntered -> {
                getMessageStatus()
            }
            MessageHomeAction.OnLifecycleStart -> {
                checkAndStartTimer()
            }
            MessageHomeAction.OnLifecycleStop -> {
                checkAndCancelTimer()
            }
        }
    }

    private fun observeProfileFlow() = intent {
        viewModelScope.launch {
            profileRepository.profileDataFlow
                .distinctUntilChanged()
                .catch { exception ->
                    Timber.e(exception)
                }
                .collect {
                    reduce { state.copy(profile = it) }
                }
        }
    }

    private fun getMessageStatus() = intent {
        reduce { state.copy(isLoading = true) }

        viewModelScope.launch {
            messageRepository.getMessageStatus()
                .onSuccess { messageStatus ->
                    /** 작성할 수 있는 쪽지가 없는 경우, 타이머를 노출한다. */
                    if (messageStatus.countRemainingMessages <= 0) {
                        startTimer()
                    } else {
                        checkAndCancelTimer()
                    }

                    reduce {
                        state.copy(messageStatus = messageStatus)
                    }
                }.onNetworkFailure {
                    postSideEffect(it.toSideEffect())
                }.also {
                    reduce { state.copy(isLoading = false) }
                }
        }
    }

    private fun getMessageHomeTitle() = intent {
        viewModelScope.launch {
            val homeTitle = getMessageHomeTitle.invoke()
            reduce {
                state.copy(homeTitle = homeTitle)
            }
        }
    }

    private fun startTimer() = intent {
        if (!timerJob.isActive) {
            _remainingTimeMillis.value = getRemainingTimeMillis()
            timerJob.start()
        }
    }

    private fun checkAndStartTimer() = intent {
        if (state.messageStatus.countRemainingMessages <= 0) {
            startTimer()
        }
    }

    private fun checkAndCancelTimer() = intent {
        if (timerJob.isActive) {
            timerJob.cancel()
        }
    }

    private fun getRemainingTimeMillis(): Long {
        val currentTimeMillis = System.currentTimeMillis() + MILLIS_KTC_OFFSET
        val elapsedMillis = currentTimeMillis % MILLIS_PER_DAY

        return MILLIS_MIDNIGHT - elapsedMillis
    }

    companion object {
        private const val MILLIS_PER_DAY = 24 * 3600 * 1000
        private const val MILLIS_MIDNIGHT = 24 * 3600 * 1000
        private const val MILLIS_KTC_OFFSET = 9 * 3600 * 1000
    }
}

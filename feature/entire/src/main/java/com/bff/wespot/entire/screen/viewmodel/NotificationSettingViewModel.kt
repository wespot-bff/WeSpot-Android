package com.bff.wespot.entire.screen.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bff.wespot.domain.repository.user.UserRepository
import com.bff.wespot.entire.screen.state.notification.NotificationSettingAction
import com.bff.wespot.entire.screen.state.notification.NotificationSettingUiState
import com.bff.wespot.model.user.response.NotificationSetting
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.reduce
import org.orbitmvi.orbit.viewmodel.container
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class NotificationSettingViewModel @Inject constructor(
    private val userRepository: UserRepository,
) : ViewModel(), ContainerHost<NotificationSettingUiState, NotificationSettingAction> {
    override val container = container<NotificationSettingUiState, NotificationSettingAction>(
        NotificationSettingUiState(),
    )

    fun onAction(action: NotificationSettingAction) {
        when (action) {
            NotificationSettingAction.OnNotificationSettingScreenEntered -> {
                handleScreenEntered()
            }
            is NotificationSettingAction.OnVoteNotificationSwitched -> {
                handleVoteNotificationSwitched()
            }
            is NotificationSettingAction.OnMessageNotificationSwitched -> {
                handleMessageNotificationSwitched()
            }
            is NotificationSettingAction.OnEventNotificationSwitched -> {
                handleEventNotificationSwitched()
            }
            NotificationSettingAction.OnNotificationSettingScreenExited -> {
                postNotificationSetting()
            }
        }
    }

    private fun handleScreenEntered() = intent {
        if (state.hasScreenBeenEntered) {
            return@intent
        }
        reduce {
            state.copy(isLoading = true, hasScreenBeenEntered = true)
        }

        viewModelScope.launch {
            userRepository.getNotificationSetting()
                .onSuccess { setting ->
                    reduce {
                        state.copy(
                            isLoading = false,
                            initialNotificationSetting = setting,
                            isEnableVoteNotification = setting.isEnableVoteNotification,
                            isEnableMessageNotification = setting.isEnableMessageNotification,
                            isEnableMarketingNotification = setting.isEnableMarketingNotification,
                        )
                    }
                }
                .onFailure {
                    reduce { state.copy(isLoading = false) }
                    Timber.e(it)
                }
        }
    }

    private fun handleVoteNotificationSwitched() = intent {
        reduce { state.copy(isEnableVoteNotification = state.isEnableVoteNotification.not()) }
    }

    private fun handleMessageNotificationSwitched() = intent {
        reduce { state.copy(isEnableMessageNotification = state.isEnableMessageNotification.not()) }
    }

    private fun handleEventNotificationSwitched() = intent {
        reduce {
            state.copy(isEnableMarketingNotification = state.isEnableMarketingNotification.not())
        }
    }

    private fun postNotificationSetting() = intent {
        val updatedNotificationSetting = NotificationSetting(
            isEnableVoteNotification = state.isEnableVoteNotification,
            isEnableMessageNotification = state.isEnableMessageNotification,
            isEnableMarketingNotification = state.isEnableMarketingNotification,
        )

        if (updatedNotificationSetting != state.initialNotificationSetting) {
            viewModelScope.launch {
                userRepository.updateNotificationSetting(updatedNotificationSetting)
                    .onFailure {
                        Timber.e(it)
                    }
            }
        }
    }
}

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
                handleVoteNotificationSwitched(action.isSwitched)
            }
            is NotificationSettingAction.OnMessageNotificationSwitched -> {
                handleMessageNotificationSwitched(action.isSwitched)
            }
            is NotificationSettingAction.OnEventNotificationSwitched -> {
                handleEventNotificationSwitched(action.isSwitched)
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

    private fun handleVoteNotificationSwitched(isSwitched: Boolean) = intent {
        reduce { state.copy(isEnableVoteNotification = isSwitched) }
    }

    private fun handleMessageNotificationSwitched(isSwitched: Boolean) = intent {
        reduce { state.copy(isEnableMessageNotification = isSwitched) }
    }

    private fun handleEventNotificationSwitched(isSwitched: Boolean) = intent {
        reduce { state.copy(isEnableMarketingNotification = isSwitched) }
    }

    private fun postNotificationSetting() = intent {
        viewModelScope.launch {
            userRepository.updateNotificationSetting(
                NotificationSetting(
                    isEnableVoteNotification = state.isEnableVoteNotification,
                    isEnableMessageNotification = state.isEnableMessageNotification,
                    isEnableMarketingNotification = state.isEnableMarketingNotification,
                ),
            ).onFailure {
                Timber.e(it)
            }
        }
    }
}

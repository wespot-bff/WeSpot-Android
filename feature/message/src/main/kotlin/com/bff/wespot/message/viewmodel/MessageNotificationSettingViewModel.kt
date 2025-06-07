package com.bff.wespot.message.viewmodel

import androidx.lifecycle.viewModelScope
import com.bff.wespot.common.extension.onNetworkFailure
import com.bff.wespot.domain.repository.user.UserRepository
import com.bff.wespot.message.state.setting.MessageNotificationSettingAction
import com.bff.wespot.message.state.setting.MessageNotificationSettingUiState
import com.bff.wespot.ui.base.BaseViewModel
import com.bff.wespot.ui.base.NoneSideEffect
import com.bff.wespot.ui.model.SideEffect.Companion.toSideEffect
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import org.orbitmvi.orbit.Container
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.reduce
import org.orbitmvi.orbit.viewmodel.container
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class MessageNotificationSettingViewModel @Inject constructor(
    private val repository: UserRepository,
) : BaseViewModel(), ContainerHost<MessageNotificationSettingUiState, NoneSideEffect> {
    override val container: Container<MessageNotificationSettingUiState, NoneSideEffect> =
        container(MessageNotificationSettingUiState()) {
            getNotificationSetting()
        }

    fun onAction(action: MessageNotificationSettingAction) {
        when (action) {
            MessageNotificationSettingAction.OnNotificationSettingSwitched -> {
                handleNotificationSettingSwitched()
            }
            MessageNotificationSettingAction.OnLifecycleStop -> {
                updateNotificationSetting()
            }
        }
    }

    private fun getNotificationSetting() = intent {
        viewModelScope.launch {
            repository.getNotificationSetting()
                .onSuccess {
                    reduce {
                        state.copy(notificationSetting = it)
                    }
                }
                .onNetworkFailure {
                    postSideEffect(it.toSideEffect())
                }
                .also {
                    reduce {
                        state.copy(isLoading = false)
                    }
                }
        }
    }

    private fun handleNotificationSettingSwitched() = intent {
        val updatedSetting = with(state.notificationSetting) {
            copy(isEnableMessageNotification = !isEnableMessageNotification)
        }
        reduce {
            state.copy(notificationSetting = updatedSetting)
        }
    }

    private fun updateNotificationSetting() = intent {
        viewModelScope.launch {
            repository.updateNotificationSetting(state.notificationSetting)
                .onFailure {
                    Timber.e(it)
                }
        }
    }
}

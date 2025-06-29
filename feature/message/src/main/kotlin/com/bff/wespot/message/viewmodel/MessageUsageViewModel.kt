package com.bff.wespot.message.viewmodel

import androidx.lifecycle.viewModelScope
import com.bff.wespot.common.extension.onNetworkFailure
import com.bff.wespot.domain.repository.message.MessageRepository
import com.bff.wespot.domain.repository.message.MessageSettingRepository
import com.bff.wespot.message.state.setting.MessageUsageSettingAction
import com.bff.wespot.message.state.setting.MessageUsageSettingUiState
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
class MessageUsageViewModel @Inject constructor(
    private val repository: MessageRepository,
    private val settingRepository: MessageSettingRepository,
) : BaseViewModel(), ContainerHost<MessageUsageSettingUiState, NoneSideEffect> {
    override val container: Container<MessageUsageSettingUiState, NoneSideEffect> =
        container(MessageUsageSettingUiState()) {
            getMessageStatus()
        }

    fun onAction(action: MessageUsageSettingAction) {
        when (action) {
            MessageUsageSettingAction.OnUsageSettingSwitched -> {
                handleUsageSettingSwitched()
            }
            MessageUsageSettingAction.OnLifecycleStop -> {
                updateMessageUsageStatus()
            }
        }
    }

    private fun getMessageStatus() = intent {
        viewModelScope.launch {
            repository.getMessageStatus()
                .onSuccess {
                    reduce {
                        state.copy(
                            initialState = it,
                            isUsageEnabled = it.isReceivedAllowed,
                        )
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

    private fun handleUsageSettingSwitched() = intent {
        reduce {
            state.copy(isUsageEnabled = state.isUsageEnabled.not())
        }
    }

    private fun updateMessageUsageStatus() = intent {
        if (state.initialState.isReceivedAllowed == state.isUsageEnabled) {
            return@intent
        }

        viewModelScope.launch {
            settingRepository.updateMessageUsageStatus(
                enabled = state.isUsageEnabled,
            ).onFailure {
                Timber.e(it)
            }
        }
    }
}

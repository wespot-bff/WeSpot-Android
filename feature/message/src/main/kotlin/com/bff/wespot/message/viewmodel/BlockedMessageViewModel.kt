package com.bff.wespot.message.viewmodel

import androidx.lifecycle.viewModelScope
import com.bff.wespot.common.extension.onNetworkFailure
import com.bff.wespot.designsystem.component.indicator.WSToastType
import com.bff.wespot.domain.repository.message.MessageSettingRepository
import com.bff.wespot.message.R
import com.bff.wespot.message.state.setting.BlockedMessageAction
import com.bff.wespot.message.state.setting.BlockedMessageSideEffect
import com.bff.wespot.message.state.setting.BlockedMessageUiState
import com.bff.wespot.ui.base.BaseViewModel
import com.bff.wespot.ui.model.SideEffect.Companion.toSideEffect
import com.bff.wespot.ui.model.ToastState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.postSideEffect
import org.orbitmvi.orbit.syntax.simple.reduce
import org.orbitmvi.orbit.viewmodel.container
import javax.inject.Inject

@HiltViewModel
class BlockedMessageViewModel @Inject constructor(
    private val repository: MessageSettingRepository,
) : BaseViewModel(), ContainerHost<BlockedMessageUiState, BlockedMessageSideEffect> {
    override val container =
        container<BlockedMessageUiState, BlockedMessageSideEffect>(BlockedMessageUiState())

    fun onAction(action: BlockedMessageAction) {
        when (action) {
            BlockedMessageAction.OnScreenEntered -> getBlockedMessageList()
            is BlockedMessageAction.OnUnBlockButtonClicked -> handleUnBlockButtonClicked(action.id)
        }
    }

    private fun getBlockedMessageList() = intent {
        reduce { state.copy(isLoading = true) }

        viewModelScope.launch {
            repository.getBlockedMessageList()
                .onSuccess {
                    reduce {
                        state.copy(messageList = it)
                    }
                }.onNetworkFailure {
                    postSideEffect(it.toSideEffect())
                }.also {
                    reduce { state.copy(isLoading = false) }
                }
        }
    }

    private fun handleUnBlockButtonClicked(messageId: Int) = intent {
        /** 이미 차단 해제를 수행한 쪽지 의 경우 다시 해제 처리하지 않는다. */
        if (state.messageList.find { it.id == messageId }?.isBlocked == false) {
            return@intent
        }

        viewModelScope.launch {
            repository.updateMessageBlockStatus(messageId)
                .onSuccess {
                    val updatedMessageList = state.messageList.map { message ->
                        if (message.id == messageId) {
                            message.copy(isBlocked = false)
                        } else {
                            message
                        }
                    }
                    reduce {
                        state.copy(messageList = updatedMessageList)
                    }

                    postSideEffect(
                        BlockedMessageSideEffect.ShowToast(
                            toastState = ToastState(
                                show = true,
                                message = R.string.unblock_done,
                                type = WSToastType.Success,
                            ),
                        ),
                    )
                }.onNetworkFailure {
                    postSideEffect(it.toSideEffect())
                }.also {
                    reduce { state.copy(isLoading = false) }
                }
        }
    }
}

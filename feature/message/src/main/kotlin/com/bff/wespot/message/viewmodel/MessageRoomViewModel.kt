package com.bff.wespot.message.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.bff.wespot.common.extension.onNetworkFailure
import com.bff.wespot.domain.repository.message.MessageStorageRepository
import com.bff.wespot.message.state.room.RoomAction
import com.bff.wespot.message.state.room.RoomSideEffect
import com.bff.wespot.message.state.room.RoomUiState
import com.bff.wespot.model.message.response.MessageDetail
import com.bff.wespot.ui.base.BaseViewModel
import com.bff.wespot.ui.model.SideEffect.Companion.toSideEffect
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.postSideEffect
import org.orbitmvi.orbit.syntax.simple.reduce
import org.orbitmvi.orbit.viewmodel.container
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class MessageRoomViewModel @Inject constructor(
    private val repository: MessageStorageRepository,
    private val savedStateHandle: SavedStateHandle,
) : BaseViewModel(),
    ContainerHost<RoomUiState, RoomSideEffect> {
    override val container = container<RoomUiState, RoomSideEffect>(RoomUiState())

    fun onAction(action: RoomAction) = intent {
        when (action) {
            is RoomAction.OnScreenEntered -> handleScreenEntered()
            is RoomAction.OnMessageDetailSelected -> handleMessageDetailSelected(action.messageDetail)
            is RoomAction.OnReplyButtonClicked -> handleReplyButtonClicked()
            is RoomAction.OnTopBarNavigate -> handleTopBarNavigate()
            is RoomAction.OnDeleteButtonClicked -> handleDeleteButtonClicked()
            is RoomAction.OnDeleteConfirmed -> handleDeleteConfirmed()
            is RoomAction.OnClosedModalButtonClicked -> handleCloseModalButtonClicked()
            RoomAction.OnNoticeModalOkButtonClicked -> {
                intent {
                    postSideEffect(RoomSideEffect.NavigateToMessageWriteScreen)
                }
            }
            RoomAction.OnNoticeModalCloseButtonClicked -> {
                intent {
                    postSideEffect(RoomSideEffect.CloseReplyNoticeModal)
                }
            }
        }
    }

    private fun handleScreenEntered() = intent {
        val roomId: Int = savedStateHandle["roomId"] ?: return@intent

        viewModelScope.launch {
            repository
                .getMessageRoom(roomId)
                .onSuccess {
                    val lastItem = it.messageDetails.lastOrNull()
                    reduce {
                        state.copy(
                            messageRoom = it,
                            selectedMessageDetail = lastItem,
                        )
                    }
                }.onNetworkFailure {
                    postSideEffect(it.toSideEffect())
                }.onFailure {
                    Timber.d(it)
                }

            launch {
                repository.hasSentReply()
                    .onSuccess {
                        reduce { state.copy(hasSentReply = it) }
                    }
            }
        }
    }

    private fun handleMessageDetailSelected(messageDetail: MessageDetail) = intent {
        reduce {
            state.copy(selectedMessageDetail = messageDetail)
        }
    }

    private fun handleReplyButtonClicked() = intent {
        if (state.hasSentReply) {
            postSideEffect(RoomSideEffect.ShowReplyNoticeModal)
            return@intent
        }
        postSideEffect(RoomSideEffect.NavigateToMessageWriteScreen)
    }

    private fun handleTopBarNavigate() = intent {
        postSideEffect(RoomSideEffect.NavigateUp)
    }

    private fun handleDeleteButtonClicked() = intent {
        postSideEffect(RoomSideEffect.ShowMessageDeleteConfirmModal)
    }

    private fun handleDeleteConfirmed() = intent {
        postSideEffect(RoomSideEffect.CloseMessageDeleteConfirmModal)
        val messageDetails = state.messageRoom.messageDetails
        val deletedMessageId = state.selectedMessageDetail?.id

        /** 삭제할 아이템이 없거나, 마지막 아이템인 경우 삭제 처리하지 않는다. */
        if (deletedMessageId == null || state.messageRoom.isSingleMessage()) {
            return@intent
        }

        viewModelScope.launch {
            repository.deleteMessage(deletedMessageId)
        }

        /** 삭제 후 삭제된 쪽지의 이전 쪽지가 선택된다. */
        val updatedMessages = messageDetails.filter { it.id != deletedMessageId }
        val deletedIndex = messageDetails.indexOfFirst { it.id == deletedMessageId }

        val selectedIndex = (deletedIndex - 1).coerceAtLeast(0)
        val selectedMessageDetail = updatedMessages.getOrNull(selectedIndex)
        reduce {
            state.copy(
                messageRoom = state.messageRoom.copy(messageDetails = updatedMessages),
                selectedMessageDetail = selectedMessageDetail,
            )
        }
    }

    private fun handleCloseModalButtonClicked() = intent {
        postSideEffect(RoomSideEffect.CloseMessageDeleteConfirmModal)
    }
}

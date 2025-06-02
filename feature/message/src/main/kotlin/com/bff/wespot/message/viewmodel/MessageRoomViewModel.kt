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
) : BaseViewModel(), ContainerHost<RoomUiState, RoomSideEffect> {
    override val container = container<RoomUiState, RoomSideEffect>(RoomUiState()) {
        val roomId: Int = savedStateHandle["roomId"] ?: return@container
        getMessageRoom(roomId)
    }

    private fun getMessageRoom(roomId: Int) = intent {
        viewModelScope.launch {
            repository.getMessageRoom(roomId)
                .onSuccess {
                    val lastItem = it.messageDetails.lastOrNull()
                    reduce {
                        state.copy(
                            messageRoom = it,
                            selectedMessageDetail = lastItem,
                        )
                    }

                    /** 쪽지 방에서 마지막 아이템 조회 처리한다. */
                    if (lastItem?.isRead == false) {
                        updateReadStatus(lastItem.id)
                    }
                }
                .onNetworkFailure {
                    postSideEffect(it.toSideEffect())
                }
                .onFailure {
                    Timber.d(it)
                }
        }
    }

    private fun updateReadStatus(messageId: Int) = intent {
        viewModelScope.launch {
            repository.updateMessageReadStatus(messageId)

            val updatedList = state.messageRoom.messageDetails.map { message ->
                if (message.id == messageId) {
                    message.copy(isRead = true)
                } else {
                    message
                }
            }

            reduce {
                state.copy(messageRoom = state.messageRoom.copy(messageDetails = updatedList))
            }
        }
    }

    fun onAction(action: RoomAction) = intent {
        when (action) {
            is RoomAction.OnMessageDetailSelected -> handleMessageDetailSelected(action.messageDetail)
            is RoomAction.OnReplyButtonClicked -> handleReplyButtonClicked()
            is RoomAction.OnTopBarNavigate -> handleTopBarNavigate()
            is RoomAction.OnDeleteButtonClicked -> handleDeleteButtonClicked()
            is RoomAction.OnDeleteConfirmed -> handleDeleteConfirmed()
            is RoomAction.OnClosedModalButtonClicked -> handleCloseModalButtonClicked()
        }
    }

    private fun handleMessageDetailSelected(messageDetail: MessageDetail) = intent {
        reduce {
            state.copy(selectedMessageDetail = messageDetail)
        }

        if (!messageDetail.isRead) {
            updateReadStatus(messageId = messageDetail.id)
        }
    }

    private fun handleReplyButtonClicked() = intent {
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

        /** 쪽지 삭제 API를 호출한다. Optimistic Update */
        viewModelScope.launch {
            repository.deleteMessage(deletedMessageId)
        }

        /**
         * 선택된 쪽지를 제거한 후, 쪽지 목록을 갱신하고 새로 선택될 쪽지를 선택한다.
         * 새롭게 선택되는 쪽지는 삭제된 쪽지의 이전 쪽지이다.
         */
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

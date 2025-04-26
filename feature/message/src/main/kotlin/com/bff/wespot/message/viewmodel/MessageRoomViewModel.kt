package com.bff.wespot.message.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.bff.wespot.common.extension.onNetworkFailure
import com.bff.wespot.domain.repository.message.MessageStorageRepository
import com.bff.wespot.message.state.room.RoomAction
import com.bff.wespot.message.state.room.RoomSideEffect
import com.bff.wespot.message.state.room.RoomUiState
import com.bff.wespot.model.message.response.MessageRoom
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
    override val container = container<RoomUiState, RoomSideEffect>(RoomUiState())

    init {
        getMessageRoom()
    }

    private fun getMessageRoom() = intent {
        val receiverId: Int = savedStateHandle["receiverId"] ?: return@intent

        viewModelScope.launch {
            repository.getMessageRoom(receiverId)
                .onSuccess {
                    reduce {
                        state.copy(
                            messageRoom = it,
                            selectedMessageDetail = it.messageDetails.lastOrNull()
                                ?: MessageRoom.MessageDetail(),
                        )
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

    private fun handleMessageDetailSelected(messageDetail: MessageRoom.MessageDetail) = intent {
        reduce {
            state.copy(selectedMessageDetail = messageDetail)
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
        val updatedMessageDetails = state.messageRoom.messageDetails.filterNot {
            it.id == state.selectedMessageDetail.id
        }

        reduce {
            state.copy(
                messageRoom = state.messageRoom.copy(
                    messageDetails = updatedMessageDetails,
                ),
            )
        }

        viewModelScope.launch {
            repository.deleteMessage(state.selectedMessageDetail.id)
                .onFailure {
                    Timber.d(it)
                }
        }
    }

    private fun handleCloseModalButtonClicked() = intent {
        postSideEffect(RoomSideEffect.CloseMessageDeleteConfirmModal)
    }
}

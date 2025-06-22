package com.bff.wespot.message.state.room

import com.bff.wespot.model.message.response.MessageDetail

sealed interface RoomAction {
    data object OnScreenEntered : RoomAction
    data class OnMessageDetailSelected(val messageDetail: MessageDetail) : RoomAction
    data object OnReplyButtonClicked : RoomAction
    data object OnTopBarNavigate : RoomAction
    data object OnDeleteButtonClicked : RoomAction
    data object OnDeleteConfirmed : RoomAction
    data object OnClosedModalButtonClicked : RoomAction
}

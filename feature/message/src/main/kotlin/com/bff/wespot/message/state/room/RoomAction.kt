package com.bff.wespot.message.state.room

import com.bff.wespot.model.message.response.MessageRoom

sealed interface RoomAction {
    data class OnMessageDetailSelected(val messageDetail: MessageRoom.MessageDetail) : RoomAction
    data object OnReplyButtonClicked : RoomAction
    data object OnTopBarNavigate : RoomAction
    data object OnDeleteButtonClicked : RoomAction
    data object OnDeleteConfirmed : RoomAction
    data object OnClosedModalButtonClicked : RoomAction
}

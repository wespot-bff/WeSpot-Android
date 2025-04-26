package com.bff.wespot.message.state.room

import com.bff.wespot.model.message.response.MessageRoom

data class RoomUiState(
    val messageRoom: MessageRoom = MessageRoom(),
    val selectedMessageDetail: MessageRoom.MessageDetail = MessageRoom.MessageDetail(),
)

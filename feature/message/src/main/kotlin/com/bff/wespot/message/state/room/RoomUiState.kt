package com.bff.wespot.message.state.room

import com.bff.wespot.model.message.response.MessageDetail
import com.bff.wespot.model.message.response.MessageRoom

data class RoomUiState(
    val messageRoom: MessageRoom = MessageRoom(),
    val hasSentReply: Boolean = false,
    val selectedMessageDetail: MessageDetail? = null,
)

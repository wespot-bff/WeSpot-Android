package com.bff.wespot.message.state.storage

import com.bff.wespot.model.message.response.MessageList

data class StorageUiState(
    val receivedMessageList: MessageList = MessageList(listOf(), true),
)

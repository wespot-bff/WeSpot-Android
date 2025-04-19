package com.bff.wespot.data.remote.model.message.response

data class MessageListDto(
    val messages: List<MessageDto>,
    val lastCursorId: Int,
    val hasNext: Boolean,
)

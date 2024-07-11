package com.bff.wespot.model.message.response

data class MessageStatus(
    val canSend: Boolean,
    val remainingMessages: Int,
)

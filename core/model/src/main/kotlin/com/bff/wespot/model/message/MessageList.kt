package com.bff.wespot.model.message

data class MessageList(
    val messages: List<Message>,
    val isLastPage: Boolean,
)

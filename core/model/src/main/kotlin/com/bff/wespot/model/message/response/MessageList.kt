package com.bff.wespot.model.message.response

data class MessageList(
    val messages: List<Message> = listOf(),
    val isLastPage: Boolean = true,
) {
    fun hasUnReadMessages(): Boolean = messages.any { it.readAt == null }
}

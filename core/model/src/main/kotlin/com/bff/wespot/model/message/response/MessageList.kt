package com.bff.wespot.model.message.response

data class MessageList(
    val messages: List<Message>,
    val lastCursorId: Int,
    val hasNext: Boolean,
) {
    fun hasUnReadMessages(): Boolean = messages.any { it.readAt == null }
}

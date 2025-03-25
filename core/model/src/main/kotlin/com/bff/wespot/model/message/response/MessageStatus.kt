package com.bff.wespot.model.message.response

data class MessageStatus(
    val isSendAllowed: Boolean,
    val countRemainingMessages: Int,
    val countUnReadMessages: Int,
) {
    fun hasUnReadMessages() = countUnReadMessages > 0
}

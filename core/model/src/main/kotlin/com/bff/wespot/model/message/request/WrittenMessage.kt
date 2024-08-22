package com.bff.wespot.model.message.request

data class WrittenMessage(
    val receiverId: Int,
    val content: String,
    val senderName: String,
    val isAnonymous: Boolean,
)

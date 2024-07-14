package com.bff.wespot.model.message.response

data class Message(
    val id: Int,
    val senderName: String,
    val content: String,
    val receivedAt: String,
    val read: Boolean,
    val readAt: String?,
)

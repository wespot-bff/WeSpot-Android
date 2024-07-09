package com.bff.wespot.model.message

data class Message(
    val id: Int,
    val senderName: String,
    val content: String,
    val receivedAt: String,
    val read: Boolean,
    val readAt: String,
)

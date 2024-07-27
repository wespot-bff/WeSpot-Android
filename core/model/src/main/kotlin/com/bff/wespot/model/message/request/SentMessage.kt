package com.bff.wespot.model.message.request

data class SentMessage(
    val receiverId: Int,
    val content: String,
    val sender: String,
)

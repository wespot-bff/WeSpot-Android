package com.bff.wespot.model.message.request

data class SendMessage(
    val receiverId: Int,
    val content: String,
    val isAnonymous: Boolean,
    val imageUrl: String,
    val name: String,
)

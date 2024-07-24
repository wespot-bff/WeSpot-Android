package com.bff.wespot.data.remote.model.message.request

import kotlinx.serialization.Serializable

@Serializable
data class SentMessageDto (
    val receiverId: Int,
    val content: String,
    val sender: String,
)

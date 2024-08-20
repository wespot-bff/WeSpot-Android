package com.bff.wespot.data.remote.model.message.request

import kotlinx.serialization.Serializable

@Serializable
data class WrittenMessageDto (
    val receiverId: Int,
    val content: String,
    val senderName: String,
    val isAnonymous: Boolean,
)

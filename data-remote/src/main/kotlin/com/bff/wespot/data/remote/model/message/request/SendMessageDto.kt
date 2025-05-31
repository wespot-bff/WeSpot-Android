package com.bff.wespot.data.remote.model.message.request

import kotlinx.serialization.Serializable

@Serializable
data class SendMessageDto (
    val receiverId: Int,
    val content: String,
    val isAnonymous: Boolean,
    val anonymousImageUrl: String,
    val anonymousProfileName: String,
)

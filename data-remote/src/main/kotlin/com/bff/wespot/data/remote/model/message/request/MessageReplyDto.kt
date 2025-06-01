package com.bff.wespot.data.remote.model.message.request

import kotlinx.serialization.Serializable

@Serializable
data class MessageReplyDto(
    val content: String,
)

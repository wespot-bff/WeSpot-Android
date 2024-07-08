package com.bff.wespot.network.model.message

import kotlinx.serialization.Serializable

@Serializable
data class MessageListDto (
    val messages: List<MessageDto>,
    val isLastPage: Boolean,
)

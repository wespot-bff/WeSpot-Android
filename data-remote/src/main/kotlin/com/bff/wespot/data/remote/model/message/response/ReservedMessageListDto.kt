package com.bff.wespot.data.remote.model.message.response

import kotlinx.serialization.Serializable

@Serializable
data class ReservedMessageListDto (
    val messages: List<MessageDto>,
)
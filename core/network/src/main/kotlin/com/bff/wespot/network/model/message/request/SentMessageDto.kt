package com.bff.wespot.network.model.message.request

import kotlinx.serialization.Serializable

@Serializable
data class SentMessageDto (
    val receivedId: Int,
    val schoolId: Int,
    val grade: Int,
    val group: Int,
    val content: String,
    val sender: String,
)

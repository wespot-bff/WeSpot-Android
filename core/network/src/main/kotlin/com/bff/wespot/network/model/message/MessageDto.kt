package com.bff.wespot.network.model.message

import kotlinx.serialization.Serializable

@Serializable
data class MessageDto (
    val id: Int,
    val senderName: String,
    val content: String,
    val receivedAt: String,
    val read: Boolean,
    val readAt: String,
)
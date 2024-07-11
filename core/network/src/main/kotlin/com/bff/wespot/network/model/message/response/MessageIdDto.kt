package com.bff.wespot.network.model.message.response

import com.bff.wespot.model.message.response.MessageId
import kotlinx.serialization.Serializable

@Serializable
data class MessageIdDto (
    val id: String,
) {
    fun toMessageId(): MessageId = MessageId(id)
}

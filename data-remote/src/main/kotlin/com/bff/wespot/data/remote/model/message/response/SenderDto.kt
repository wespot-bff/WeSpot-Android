package com.bff.wespot.data.remote.model.message.response

import com.bff.wespot.model.message.Sender
import kotlinx.serialization.Serializable

@Serializable
data class SenderDto (
    val id: Int,
    val backgroundColor: String,
    val iconUrl: String,
) {
    fun toSender(): Sender = Sender(
        id = id,
        backgroundColor = backgroundColor,
        iconUrl = iconUrl,
    )
}
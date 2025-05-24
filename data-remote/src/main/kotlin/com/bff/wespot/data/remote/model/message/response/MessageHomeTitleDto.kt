package com.bff.wespot.data.remote.model.message.response

import com.bff.wespot.model.message.response.MessageHomeTitle
import kotlinx.serialization.Serializable

@Serializable
data class MessageHomeTitleDto(
    val title: String
) {
    fun toMessageHomeTitle() = MessageHomeTitle(
        title = title
    )
}

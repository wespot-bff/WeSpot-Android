package com.bff.wespot.data.remote.model.message.request

enum class MessageTypeDto {
    RECEIVED,
    SENT,
}

internal fun MessageTypeDto.type() = this.name.lowercase()

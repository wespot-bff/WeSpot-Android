package com.bff.wespot.network.model.message.request

enum class MessageTypeDto {
    RECEIVED,
    SENT,
}

fun MessageTypeDto.name() = this.name.lowercase()

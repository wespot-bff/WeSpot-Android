package com.bff.wespot.model.message

enum class MessageType {
    RECEIVED,
    SENT,
}

fun MessageType.name() = this.name.lowercase()

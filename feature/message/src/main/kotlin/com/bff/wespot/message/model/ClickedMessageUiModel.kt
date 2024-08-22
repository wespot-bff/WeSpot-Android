package com.bff.wespot.message.model

data class ClickedMessageUiModel(
    val content: String,
    val receiver: String,
    val sender: String,
) {
    constructor() : this("", "", "")
}

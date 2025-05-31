package com.bff.wespot.model.message.response

data class Sender(
    val id: Int,
    val backgroundColor: String,
    val iconUrl: String,
) {
    constructor() : this(-1, "", "")
}

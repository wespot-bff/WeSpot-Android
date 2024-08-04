package com.bff.wespot.model.message

data class Sender(
    val id: Int,
    val backgroundColor: String,
    val iconUrl: String,
) {
    constructor() : this(-1, "", "")
}

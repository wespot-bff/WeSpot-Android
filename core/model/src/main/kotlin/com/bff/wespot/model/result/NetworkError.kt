package com.bff.wespot.model.result

data class NetworkError(
    val type: String,
    val title: String,
    val status: Int,
    val detail: String,
    val instance: String,
) : BaseError

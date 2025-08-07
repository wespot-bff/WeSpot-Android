package com.bff.wespot.data.remote.model.common

import kotlinx.serialization.Serializable

@Serializable
data class ImageUrlDto(
    val url: String,
    val imageName: String
)
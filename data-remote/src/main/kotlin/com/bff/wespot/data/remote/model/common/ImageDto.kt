package com.bff.wespot.data.remote.model.common

import kotlinx.serialization.Serializable

@Serializable
data class ImageDto(
    val url: String,
    val imageUrl: String
)
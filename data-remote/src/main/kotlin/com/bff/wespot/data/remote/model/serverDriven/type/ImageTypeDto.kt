package com.bff.wespot.data.remote.model.serverDriven.type

import com.bff.wespot.model.serverDriven.type.ImageType
import kotlinx.serialization.Serializable

@Serializable
data class ImageTypeDto(
    val url: String,
    val width: Int,
    val height: Int
) {
    fun toDomain() = ImageType(
        url = url,
        width = width,
        height = height
    )
}

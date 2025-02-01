package com.bff.wespot.data.remote.model.serverDriven

import com.bff.wespot.model.serverDriven.ImageComponent
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@SerialName("imageComponent")
data class ImageComponentDto(
    val url: String,
    val width: Int,
    val height: Int
) : BaseComponentDto {
    override fun toDomain(): ImageComponent {
        return ImageComponent(
            url = url,
            width = width,
            height = height
        )
    }
}
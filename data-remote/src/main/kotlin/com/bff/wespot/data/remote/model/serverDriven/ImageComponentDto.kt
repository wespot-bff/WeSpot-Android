package com.bff.wespot.data.remote.model.serverDriven

import com.bff.wespot.model.serverDriven.ImageComponent
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@SerialName("imageComponent")
data class ImageComponentDto(
    val content: ImageComponentContent
) : BaseComponentDto {
    override fun toDomain(): ImageComponent {
        return ImageComponent(
            url = content.url,
            width = content.width,
            height = content.height,
            paddings = content.paddings.toDomain()
        )
    }
}

@Serializable
data class ImageComponentContent(
    val url: String,
    val width: Int,
    val height: Int,
    val paddings: PaddingsDto = PaddingsDto.DEFAULT,
)
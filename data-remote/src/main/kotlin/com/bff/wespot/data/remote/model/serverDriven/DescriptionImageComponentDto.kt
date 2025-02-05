package com.bff.wespot.data.remote.model.serverDriven

import com.bff.wespot.model.serverDriven.BaseComponent
import com.bff.wespot.model.serverDriven.DescriptionImageComponent
import kotlinx.serialization.SerialName

@SerialName("descriptionImageComponent")
data class DescriptionImageComponentDto(
    val url: String,
    val width: Int,
    val height: Int,
) : BaseComponentDto {
    override fun toDomain(): BaseComponent {
        return DescriptionImageComponent(
            url = url,
            width = width,
            height = height,
        )
    }
}

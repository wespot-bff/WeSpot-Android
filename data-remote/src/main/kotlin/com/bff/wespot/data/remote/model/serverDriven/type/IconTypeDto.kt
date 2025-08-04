package com.bff.wespot.data.remote.model.serverDriven.type

import com.bff.wespot.model.serverDriven.type.IconType
import kotlinx.serialization.Serializable

@Serializable
data class IconTypeDto(
    val url: String,
    val color: ColorTypeDto
) {
    fun toDomain() = IconType(
        url = url,
        color = color.toDomain()
    )
}
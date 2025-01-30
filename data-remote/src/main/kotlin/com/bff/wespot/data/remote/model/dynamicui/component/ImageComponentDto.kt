package com.bff.wespot.data.remote.model.dynamicui.component

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@SerialName("imageComponent")
data class ImageComponentDto(
    val url: String,
    val width: Int,
    val height: Int
): DynamicUiComponentDto

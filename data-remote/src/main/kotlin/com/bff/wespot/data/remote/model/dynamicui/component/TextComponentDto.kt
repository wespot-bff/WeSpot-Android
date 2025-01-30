package com.bff.wespot.data.remote.model.dynamicui.component

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@SerialName("textComponent")
data class TextComponentDto(
    val text: String,
): DynamicUiComponentDto

package com.bff.wespot.data.remote.model.dynamicui.component

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@SerialName("buttonComponent")
data class ButtonComponentDto(
    val text: String,
    val link: String = "",
): DynamicUiComponentDto

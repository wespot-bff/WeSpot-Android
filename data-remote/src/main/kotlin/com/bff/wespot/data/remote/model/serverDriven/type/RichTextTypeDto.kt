package com.bff.wespot.data.remote.model.serverDriven.type

import com.bff.wespot.model.serverDriven.type.RichTextType
import kotlinx.serialization.Serializable

@Serializable
data class RichTextTypeDto(
    val text: String,
    val color: ColorTypeDto,
    val typography: String,
    val maxLine: Int = 0
) {
    fun toDomain() = RichTextType(
        text = text,
        color = color.toDomain(),
        typography = typography,
        maxLine = maxLine
    )
}
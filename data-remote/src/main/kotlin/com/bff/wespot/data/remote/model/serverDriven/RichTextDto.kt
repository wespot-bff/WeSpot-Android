package com.bff.wespot.data.remote.model.serverDriven

import com.bff.wespot.model.serverDriven.RichText
import kotlinx.serialization.Serializable

@Serializable
data class RichTextDto(
    val text: String,
    val color: String,
    val fontSize: Int,
    val align: String = "Start",
    val fontWeight: String = "Normal"
) {
    fun toDomain() = RichText(
        text = text,
        color = color,
        fontSize = fontSize,
        align = align,
        fontWeight = fontWeight
    )
}
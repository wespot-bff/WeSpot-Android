package com.bff.wespot.data.remote.model.serverDriven

import com.bff.wespot.model.serverDriven.TextComponent
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@SerialName("textComponent")
data class TextComponentDto(
    val content: TextComponentContent
): BaseComponentDto {
    override fun toDomain() = TextComponent(
        richText = content.richText.toDomain(),
        paddings = content.paddings.toDomain()
    )
}

@Serializable
data class TextComponentContent(
    val richText: RichTextDto,
    val paddings: PaddingsDto,
)
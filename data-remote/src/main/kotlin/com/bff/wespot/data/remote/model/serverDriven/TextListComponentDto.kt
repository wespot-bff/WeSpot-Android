package com.bff.wespot.data.remote.model.serverDriven

import com.bff.wespot.model.serverDriven.TextList
import com.bff.wespot.model.serverDriven.TextListComponent
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@SerialName("textListComponent")
data class TextListComponentDto(
    val textList: List<TextListDto>
) : BaseComponentDto {
    override fun toDomain(): TextListComponent {
        return TextListComponent(
            textList = textList.map { it.toDomain() }
        )
    }
}

@Serializable
data class TextListDto(
    val icon: String,
    val text: String
) {
    fun toDomain() = TextList(
        icon = icon,
        text = text
    )
}
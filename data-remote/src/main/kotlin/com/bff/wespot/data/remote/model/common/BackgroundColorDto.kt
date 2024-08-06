package com.bff.wespot.data.remote.model.common

import com.bff.wespot.model.common.BackgroundColor
import com.bff.wespot.model.common.BackgroundColorList
import kotlinx.serialization.Serializable

@Serializable
data class BackgroundColorDto(
    val id: Int,
    val color: String,
    val name: String,
) {
    fun toBackgroundColor() = BackgroundColor(
        id = id,
        color = color,
        name = name,
    )

}

@Serializable
data class BackgroundColorListDto(
    val backgrounds: List<BackgroundColorDto>
) {
    fun toBackgroundColorList() = BackgroundColorList(
        backgrounds = backgrounds.map { it.toBackgroundColor() }
    )
}
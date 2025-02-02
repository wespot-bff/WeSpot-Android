package com.bff.wespot.data.remote.model.serverDriven

import com.bff.wespot.model.serverDriven.TitleComponent
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@SerialName("titleComponent")
data class TitleComponentDto(
    val text: String

) : BaseComponentDto {
    override fun toDomain(): TitleComponent {
        return TitleComponent(
            text = text
        )
    }
}
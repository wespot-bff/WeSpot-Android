package com.bff.wespot.data.remote.model.serverDriven

import com.bff.wespot.model.serverDriven.ButtonComponent
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@SerialName("buttonComponent")
data class ButtonComponentDto(
    val text: String
) : BaseComponentDto {
    override fun toDomain(): ButtonComponent {
        return ButtonComponent(
            text = text
        )
    }
}

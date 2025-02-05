package com.bff.wespot.data.remote.model.serverDriven

import com.bff.wespot.model.serverDriven.BaseComponent
import com.bff.wespot.model.serverDriven.DescriptionComponent
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@SerialName("descriptionComponent")
class DescriptionComponentDto(
    val text: String,
) : BaseComponentDto {
    override fun toDomain(): BaseComponent {
        return DescriptionComponent(
            text = text,
        )
    }
}

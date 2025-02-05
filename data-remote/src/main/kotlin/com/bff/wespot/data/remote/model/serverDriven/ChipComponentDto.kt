package com.bff.wespot.data.remote.model.serverDriven

import com.bff.wespot.model.serverDriven.BaseComponent
import com.bff.wespot.model.serverDriven.ChipComponent

data class ChipComponentDto(
    val text: String,
) : BaseComponentDto {
    override fun toDomain(): BaseComponent {
        return ChipComponent(
            text = text,
        )
    }
}

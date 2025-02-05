package com.bff.wespot.data.remote.model.serverDriven

import com.bff.wespot.model.serverDriven.BaseComponent
import com.bff.wespot.model.serverDriven.SubTitleComponent

data class SubTitleComponentDto(
    val text: String,
) : BaseComponentDto {
    override fun toDomain(): BaseComponent {
        return SubTitleComponent(
            text = text,
        )
    }
}

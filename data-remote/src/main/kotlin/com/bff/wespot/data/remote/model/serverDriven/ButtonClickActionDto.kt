package com.bff.wespot.data.remote.model.serverDriven

import com.bff.wespot.model.serverDriven.ClickAction
import kotlinx.serialization.Serializable

@Serializable
sealed interface ClickActionDto {
    fun toDomain(): ClickAction
}

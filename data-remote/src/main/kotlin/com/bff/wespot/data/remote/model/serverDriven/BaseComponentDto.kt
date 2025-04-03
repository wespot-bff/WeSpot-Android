package com.bff.wespot.data.remote.model.serverDriven

import com.bff.wespot.model.serverDriven.BaseComponent
import kotlinx.serialization.Serializable

@Serializable
sealed interface BaseComponentDto {
    fun toDomain(): BaseComponent
}
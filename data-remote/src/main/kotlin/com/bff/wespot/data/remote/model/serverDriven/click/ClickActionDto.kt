package com.bff.wespot.data.remote.model.serverDriven.click

import com.bff.wespot.model.serverDriven.click.ClickAction
import kotlinx.serialization.Serializable

@Serializable
sealed interface ClickActionDto {
    fun toDomain(): ClickAction
}
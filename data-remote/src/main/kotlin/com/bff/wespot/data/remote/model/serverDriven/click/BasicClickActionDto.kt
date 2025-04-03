package com.bff.wespot.data.remote.model.serverDriven.click

import com.bff.wespot.model.serverDriven.click.BasicClickAction
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@SerialName("action")
data object BasicClickActionDto : ClickActionDto {
    override fun toDomain() = BasicClickAction
}